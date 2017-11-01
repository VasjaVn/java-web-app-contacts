package com.contacts.controller;

import com.contacts.common.ModelAttributeName;
import com.contacts.common.message.Message;
import com.contacts.common.message.MessageSrcCode;
import com.contacts.common.util.MessageFactory;
import com.contacts.common.util.UrlUtil;
import com.contacts.dto.inner.ContactGrid;
import com.contacts.entity.Contact;
import com.contacts.service.ContactService;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;


@RequestMapping("/contacts")
@Controller
public class ContactController {
    private final Logger logger = LoggerFactory.getLogger(ContactController.class);

    public static final String ATTRIBUTE_NAME_MESSAGE = ModelAttributeName.MESSAGE.toString();
    public static final String ATTRIBUTE_NAME_CONTACT = ModelAttributeName.CONTACT.toString();

    private ContactService contactService;
    private MessageFactory messageFactory;

    @Autowired
    public void setContactService(ContactService contactService) {
        this.contactService = contactService;
    }

    @Autowired
    public void setMessageFactory(MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, ModelMap uiModel) {
        logger.info("Show contact: { id = " + id + " }");

        Contact contact = contactService.findById(id);
        uiModel.addAttribute(ATTRIBUTE_NAME_CONTACT, contact);
        return "contacts/show";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Delete contact: { id = " + id + " }");

        Contact contact = contactService.findById(id);
        Object[] args = { contact.getFirstName(), contact.getLastName() };

        contactService.delete(id);

        Message message = messageFactory.getMessage(MessageSrcCode.CONTACT_DELETE_SUCCESS, args, locale);
        redirectAttributes.addFlashAttribute(ATTRIBUTE_NAME_MESSAGE, message);

        return "redirect:/contacts/";
    }

    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.POST)
    public String update(@Valid Contact contact,
                         BindingResult bindingResult,
                         Model uiModel,
                         HttpServletRequest httpServletRequest,
                         RedirectAttributes redirectAttributes,
                         Locale locale,
                         @RequestParam(value="file", required=false) Part file)
    {
        logger.info("Update contact: { id = " + contact.getId() + " }");

        if ( bindingResult.hasErrors() ) {
            Message message = getMessage(MessageSrcCode.CONTACT_SAVE_FAIL, null, locale);
            uiModel.addAttribute(ATTRIBUTE_NAME_MESSAGE, message);
            uiModel.addAttribute(ATTRIBUTE_NAME_CONTACT, contact);
            return "contacts/update";
        }

        uiModel.asMap().clear();

        byte[] fileContent = null;
        if ( file != null && file.getSize() != 0 ) {
            logger.info("File name: " + file.getName());
            logger.info("File size: " + file.getSize());
            logger.info("File content type: " + file.getContentType());

            try {
                InputStream inputStream = file.getInputStream();
                if (inputStream == null) {
                    logger.info("File inputstream is null");
                }
                fileContent = IOUtils.toByteArray(inputStream);

            } catch (IOException ex) {
                logger.error("Error saving uploaded file");
            }

        } else {
            fileContent = contactService.findById(contact.getId()).getPhoto();
        }

        contact.setPhoto(fileContent);
        contactService.save(contact);

        Message message = getMessage(MessageSrcCode.CONTACT_SAVE_SUCCESS, null, locale);
        redirectAttributes.addFlashAttribute(ATTRIBUTE_NAME_MESSAGE, message);

        return "redirect:/contacts/" + UrlUtil.encodeUrlPathSegment(contact.getId().toString(), httpServletRequest);
    }

    @PreAuthorize ( "isAuthenticated () ")
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        Contact contact = contactService.findById(id);
        uiModel.addAttribute(ATTRIBUTE_NAME_CONTACT, contact);

        return "contacts/update";
    }

    @RequestMapping(method = RequestMethod.GET)

    public String list() {
        logger.info("Listing contacts");
        return "contacts/list";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid Contact contact,
                         BindingResult bindingResult,
                         Model uiModel,
                         RedirectAttributes redirectAttributes,
                         Locale locale,
                         @RequestParam(value="file", required=false) Part file)
    {
        logger.info("Creating contact");

        if (bindingResult.hasErrors()) {
            Message message = getMessage(MessageSrcCode.CONTACT_SAVE_FAIL, null, locale);
            uiModel.addAttribute(ATTRIBUTE_NAME_MESSAGE, message);
            uiModel.addAttribute(ATTRIBUTE_NAME_CONTACT, contact);
            return "contacts/create";
        }

        logger.info("Contact id: " + contact.getId());

        uiModel.asMap().clear();
        Message message = getMessage(MessageSrcCode.CONTACT_SAVE_SUCCESS, null, locale);
        redirectAttributes.addFlashAttribute(ATTRIBUTE_NAME_MESSAGE, message);

        // Process upload file
        if (file != null) {
            logger.info("File name: " + file.getName());
            logger.info("File size: " + file.getSize());
            logger.info("File content type: " + file.getContentType());

            byte[] fileContent = null;
            try {
                InputStream inputStream = file.getInputStream();
                if (inputStream == null) {
                    logger.info("File inputstream is null");
                }

                fileContent = IOUtils.toByteArray(inputStream);
            } catch (IOException ex) {
                logger.error("Error saving uploaded file");
            }
            contact.setPhoto(fileContent);
        }

        contactService.save(contact);

        return "redirect:/contacts";
    }

    @RequestMapping(value = "/photo/{id}", method = RequestMethod.GET)
    @ResponseBody
    public byte[] downloadPhoto(@PathVariable("id") Long id) {
        Contact contact = contactService.findById(id);

        if (contact.getPhoto() != null) {
            logger.info("Downloading photo for id: {} with size: {}", id, contact.getPhoto().length);
        }

        return contact.getPhoto();
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        Contact contact = new Contact();
        uiModel.addAttribute(ATTRIBUTE_NAME_CONTACT, contact);

        return "contacts/create";
    }

    @RequestMapping(value = "/listgrid", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ContactGrid listGrid(@RequestParam(value = "page", required = false) Integer page,
                                @RequestParam(value = "rows", required = false) Integer rows,
                                @RequestParam(value = "sidx", required = false) String orderBy,
                                @RequestParam(value = "sord", required = false) String orderDirection) {

        logger.info("Listing contacts for grid with page: {}, rows: {}", page, rows);
        logger.info("Listing contacts for grid with orderBy: {}, order: {}", orderBy, orderDirection);

        Sort sort = null;
        if ( orderBy != null ) {
            Sort.Direction direction = ("desc".equals(orderDirection) ? DESC : ASC);
            sort = new Sort(direction, orderBy);
        }

        // Constructs page request for current page
        // Note: page number for Spring Data JPA starts with 0, while jqGrid starts with 1
        int numberPageInSpringDataJPA = page - 1;
        PageRequest pageRequest = new PageRequest(numberPageInSpringDataJPA, rows, sort);

        Page<Contact> contactPage = contactService.findAllByPage(pageRequest);
        int numberPageInJqGrid = contactPage.getNumber() + 1;

        ContactGrid contactGrid = new ContactGrid();
        contactGrid.setCurrentPage( numberPageInJqGrid );
        contactGrid.setTotalPages( contactPage.getTotalPages() );
        contactGrid.setTotalRecords( contactPage.getTotalElements() );
        contactGrid.setContactData( Lists.newArrayList(contactPage.iterator()) );

        return contactGrid;
    }

    private Message getMessage(MessageSrcCode messageSrcCode, Object[] args, Locale locale) {
        return messageFactory.getMessage(messageSrcCode, args, locale);
    }

    /*
        @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "IOException exception!")
        @ExceptionHandler(value = Exception.class)
        public void handleIOException(Exception ex) {
            logger.info("*************** EXCEPTION **************");
            logger.info(ex.getMessage());
            logger.info("********************************************");
        }
    */
}