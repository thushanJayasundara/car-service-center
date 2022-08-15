package com.automiraj.service;

import com.automiraj.constant.Availability;
import com.automiraj.constant.CommonMsg;
import com.automiraj.constant.CommonStatus;
import com.automiraj.dto.BookingDTO;
import com.automiraj.entity.Booking;
import com.automiraj.entity.Customer;
import com.automiraj.entity.CustomerBooking;
import com.automiraj.repository.BookingRepository;
import com.automiraj.repository.CustomerRepository;
import com.automiraj.util.CommonResponse;
import com.automiraj.util.CommonValidation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service

public class BookingService {

    @Value("${sms.message.body}")
    private String smsBody;

    private final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;
    private final SlotService slotService;
    private final CustomerBookingService customerBookingService;
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final SmsService smsService;

    @Autowired
    BookingService(BookingRepository bookingRepository, ModelMapper modelMapper, SlotService slotService, CustomerBookingService customerBookingService, CustomerService customerService, CustomerRepository customerRepository, SmsService smsService) {this.bookingRepository = bookingRepository;
        this.modelMapper = modelMapper;
        this.slotService = slotService;
        this.customerBookingService = customerBookingService;
        this.customerService = customerService;
        this.customerRepository = customerRepository;
        this.smsService = smsService;
    }

/**
 * Booking save
 */

    public CommonResponse save( final BookingDTO bookingDTO) {
        CommonResponse commonResponse = new CommonResponse();
        Booking booking;
        String msg;
        CustomerBooking customerBooking = new CustomerBooking();
        Customer customer;
        try {

            List<String> validationList = this.bookingValidation(bookingDTO);
            if (!validationList.isEmpty()) {
                commonResponse.setErrorMessages(validationList);
                return commonResponse;
            }
            customer = customerService.getByNic(bookingDTO.getCustomerDTO().getNic());

            if (customerBookingService.existsByCustomerAndCommonStatus(customer,CommonStatus.ACTIVE)){
                commonResponse.setErrorMessages(Collections.singletonList("Already Booking Slot"));
                return commonResponse;
            }
                booking = castBookingDTOIntoBooking (bookingDTO);
                booking = bookingRepository.save(booking);

                customerBooking.setBooking(booking);
                customerBooking.setCustomer(customer);
                customerBooking =  customerBookingService.save(customerBooking);

                slotService.updateSlotAvailability(bookingDTO.getSlotDTO().getId(), Availability.NOT_AVAILABLE);
            msg = smsBody  + ". Slot number " +booking.getSlot().getSlotName() + ". Service type " + booking.getSlot().getMaintenanceType().getMaintenanceName() +
                    " and your time is " + booking.getSlot().getTimePeriod().getTimeCode();

                smsService.sendMessage(customerBooking.getCustomer().getMobile().toString(),msg);
                commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in CustomerService -> save()" + e);
        }
        return commonResponse;
    }

    /**
     * Booking update
     * @param bookingDTO
     * @return
     */

    public CommonResponse update (final BookingDTO bookingDTO){
        CommonResponse commonResponse = new CommonResponse();
        Booking booking;
        try {
            List<String> validationList = this.bookingValidation(bookingDTO);
            if(!validationList.isEmpty()){
                commonResponse.setErrorMessages(validationList);
               return commonResponse;
            }
            Booking exitBooking =this.getById(bookingDTO.getId());
            booking = castBookingDTOIntoBooking(bookingDTO);
            exitBooking.setId(booking.getId());
            exitBooking.setCommonStatus(booking.getCommonStatus());
            bookingRepository.save(exitBooking);
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in CustomerService -> save()" + e);
        }
        return commonResponse;
    }



    /**
     * cast BookingDTO into Booking
     * @param
     * @return
     */
    public Booking castBookingDTOIntoBooking (BookingDTO bookingDTO) {
        Booking booking = new Booking();
        booking.setSlot(slotService.findSlotById(bookingDTO.getSlotDTO().getId()));
        booking.setCommonStatus(bookingDTO.getCommonStatus());
        return booking;
    }

    public Booking getById(String bookingId){
        return bookingRepository.findById(Long.parseLong(bookingId)).get();
    }

    /**
     * booking delete
     * @param id
     * @return
     */
    public CommonResponse delete(final String id) {
        CommonResponse commonResponse = new CommonResponse();
        Booking booking;
        try {
            if (bookingRepository.existsById(Long.valueOf(id))) {
                booking = bookingRepository.findById(Long.parseLong(String.valueOf(id))).get();
                booking.setCommonStatus(CommonStatus.DELETE);
                bookingRepository.save(booking);
            } else
                commonResponse.setErrorMessages(Collections.singletonList(CommonMsg.NOT_FOUND));

        } catch (Exception e) {
            LOGGER.error("/**************** Exception in BookingService -> save()" + e);
        }
        return commonResponse;
    }

    /**
     * Booking get all
     * @return
     */
    public CommonResponse getAll() {
        CommonResponse commonResponse = new CommonResponse();
        List<BookingDTO> bookingDTOList;
        try {
            Predicate<Booking> filterOnStatus = booking -> booking.getCommonStatus() != CommonStatus.DELETE;

            bookingDTOList = bookingRepository.findAll()
                    .stream()
                    .filter(filterOnStatus)
                    .map(this::castBookingIntoBookingDTO)
                    .collect(Collectors.toList());
            commonResponse.setStatus(true);
            commonResponse.setPayload(Collections.singletonList(bookingDTOList));
        } catch (Exception e) {
            LOGGER.error("/**************** Exception in CustomerService -> save()" + e);
        }
        return commonResponse;

    }

    /**
     *
     * @return
     * @param id
     */
    public CommonResponse findById(final String id){
        CommonResponse commonResponse = new CommonResponse();
        Booking booking;
        try {
                 booking = bookingRepository.getById(Long.parseLong(id));
                commonResponse.setPayload(Collections.singletonList(castBookingIntoBookingDTO(booking)));
                commonResponse.setStatus(true);

        }catch (Exception e){
            LOGGER.error("/**************** Exception in CustomerService -> save()" + e);
        }return commonResponse;
    }

    public Booking findBookingById(String id){
        return bookingRepository.getById(Long.parseLong(id));
    }

    /**
     * Booking cast booking into bookingDTO
     * @param booking
     * @return
     */
    public BookingDTO castBookingIntoBookingDTO(Booking booking){
        BookingDTO bookingDTO = modelMapper.map(booking, BookingDTO.class);
        bookingDTO.setSlotDTO(slotService.castSlotIntoSlotDTO(booking.getSlot()));
       return bookingDTO;
    }

    /**
     * Booking validation
     * @param bookingDTO
     * @return
     */
    public List<String> bookingValidation(final BookingDTO bookingDTO) {
        List<String> validationList = new ArrayList<>();

        if (CommonValidation.isvalidNic(bookingDTO.getCustomerDTO().getNic())){
            validationList.add(CommonMsg.INVALID_NIC);
            return validationList;}

        if(!customerRepository.existsByNic(bookingDTO.getCustomerDTO().getNic())){
            validationList.add(CommonMsg.NOT_REGISTERED);
            return validationList;}

        return validationList;
    }
}