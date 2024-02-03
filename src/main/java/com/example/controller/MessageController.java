package com.example.controller;

import com.example.dto.MessageDto;
import com.example.dto.mapper.MessageMapper;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("/messages")
@PropertySource("classpath:messages.properties")
public class MessageController {

    private final AccountService accountService;
    private final MessageService messageService;
    private final MessageMapper messageMapper;

    @Value("${error.message.not.exist}")
    private String notExistMsg;
    @Value("${debug.message.account.empty}")
    private String accountEmptyMsg;
    @Value("${error.message.no.sender}")
    private String noSenderMsg;
    @Value("${debug.message.id.null}")
    private String idNullMsg;
    @Value("${error.message.text.empty}")
    private String textEmptyMsg;
    @Value("${debug.message.text.null}")
    private String textNullMsg;
    @Value("${error.message.length.exceeds}")
    private String lengthExceedsMsg;
    @Value("${info.message.create.successfully}")
    private String createSuccessfullyMsg;
    @Value("${info.message.delete.successfully}")
    private String deleteSuccessfullyMsg;
    @Value("${info.message.empty}")
    private String emptyMsg;
    @Value("${info.message.update.successfully}")
    private String updateSuccessfullyMsg;
    @Value("${error.message.update.fail}")
    private String updateFailMsg;


    public MessageController(AccountService accountService, MessageService messageService,
                             MessageMapper messageMapper) {
        this.accountService = accountService;
        this.messageService = messageService;
        this.messageMapper = messageMapper;
    }

    @GetMapping()
    public List<MessageDto> retrieveAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return messages.stream()
                .map(messageMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping()
    @ResponseBody
    public ResponseEntity<MessageDto> createMessage(@RequestBody MessageDto messageDto) {
        Message message = messageMapper.toEntity(messageDto);
        Optional<Account> account = accountService.getAccountById(message.getPosted_by());

        if (account.isEmpty()) {
            log.error(notExistMsg);
            log.debug(accountEmptyMsg);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (message.getPosted_by() == null) {
            log.error(noSenderMsg);
            log.debug(idNullMsg);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (message.getMessage_text().isEmpty()) {
            log.error(textEmptyMsg);
            log.debug(textNullMsg);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (message.getMessage_text().length() > 255) {
            log.error(lengthExceedsMsg);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        messageService.create(message);
        log.info(createSuccessfullyMsg);

        return ResponseEntity.ok(messageMapper.toDto(message));
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<MessageDto> findMessageById(@PathVariable Integer id) {
        return messageService.getMessageById(id)
                .map(message -> ResponseEntity.status(HttpStatus.OK).body(messageMapper.toDto(message)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.OK).body(null));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Integer> deleteMessage(@PathVariable Integer id) {
        if (messageService.getMessageById(id).isPresent()) {
            log.info(deleteSuccessfullyMsg);

            return ResponseEntity.ok(messageService.deleteMessageById(id));
        }
        log.info(emptyMsg);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Integer> updateMessage(@RequestBody MessageDto messageDto,
                                                 @PathVariable Integer id) {
        Optional<Message> message = messageService.getMessageById(id);
        Message entity = messageMapper.toEntity(messageDto);
        if (entity.getMessage_text().isEmpty()) {
            log.error(textEmptyMsg);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        } else if (entity.getMessage_text().length() > 255) {
            log.error(lengthExceedsMsg);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (message.isPresent()) {
            message.get().setMessage_text(entity.getMessage_text());
            log.info(updateSuccessfullyMsg);

            return ResponseEntity.ok(messageService.update(message.get().getMessage_text(), id));
        }
        log.error(updateFailMsg);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(0);
    }
}
