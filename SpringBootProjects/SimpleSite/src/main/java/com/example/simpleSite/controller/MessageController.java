package com.example.simpleSite.controller;

import com.example.simpleSite.models.Message;
import com.example.simpleSite.models.User;
import com.example.simpleSite.models.dto.MessageDto;
import com.example.simpleSite.repositories.MessageRepo;
import com.example.simpleSite.service.MessageService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
public class MessageController {
    private final MessageRepo messageRepo;
    private final MessageService messageService;
    @Value("${upload.path}")
    private String uploadPath;

    public MessageController(MessageRepo messageRepo, MessageService messageService) {
        this.messageRepo = messageRepo;
        this.messageService = messageService;
    }

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter,
                       Model model,
                       @AuthenticationPrincipal User user) {
        Iterable<MessageDto> messages = messageService.messageList(filter, user);

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("/main")
    public String addMessage(@AuthenticationPrincipal User user,
                             @Valid Message message,
                             BindingResult bindingResult,
                             Model model,
                             @RequestParam("file") MultipartFile file) throws IOException {
        message.setAuthor(user);
        if (bindingResult.hasErrors()) {

            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
            model.addAttribute("messages", messageRepo.findAll());
            return "main";
        } else {
            saveFile(message, file);
            messageRepo.save(message);
        }
        return "redirect:main";
    }

    @GetMapping("/user-message/{author}")
    public String userMessages(@AuthenticationPrincipal User currentUser,
                               @PathVariable User author,
                               @RequestParam(required = false) Message messageId,
                               Model model) {
        Set<MessageDto> messages = messageService.messageListForUser(author, currentUser);
        model.addAttribute("subscriptionsCount", author.getSubscriptions().size());
        model.addAttribute("subscribersCount", author.getSubscribers().size());
        model.addAttribute("isCurrentUser", currentUser.equals(author));
        model.addAttribute("isSubscriber", author.getSubscribers().contains(currentUser));
        model.addAttribute("userChanel", author);
        model.addAttribute("messages", messages);
        model.addAttribute("message", messageId);

        return "userMessages";
    }

    @PostMapping("/user-message/{user}")
    public String updateMessage(@AuthenticationPrincipal User currentUser,
                                @PathVariable Long user,
                                @RequestParam Message messageId,
                                @RequestParam String text,
                                @RequestParam String tag,
                                @RequestParam("file") MultipartFile file) throws IOException {
        if (messageId.getAuthor().equals(currentUser)) {
            if (!Strings.isBlank(text)) {
                messageId.setText(text);
            }
            if (!Strings.isBlank(tag)) {
                messageId.setTag(tag);
            }
            saveFile(messageId, file);
            messageRepo.save(messageId);
        }

        return "redirect:/user-message/" + user;
    }

    private void saveFile(Message message, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));
            message.setFileName(resultFilename);
        }
    }

    @GetMapping("/messages/{message}/like")
    public String like(@AuthenticationPrincipal User currentUser,
                       @PathVariable Message message,
                       RedirectAttributes redirectAttributes,
                       @RequestHeader(required = false) String referer) {

        Set<User> likes = message.getLikes();
        if (likes.contains(currentUser)) {
            likes.remove(currentUser);

        } else {
            likes.add(currentUser);
        }
        messageRepo.save(message);
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));
        return "redirect:" + components.getPath();
    }

}
