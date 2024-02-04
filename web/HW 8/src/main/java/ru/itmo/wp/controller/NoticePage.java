package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.form.NoticeData;
import ru.itmo.wp.form.validator.NoticeDataCreationValidator;
import ru.itmo.wp.service.NoticeService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class NoticePage extends Page {
    private final NoticeService noticeService;
    private final NoticeDataCreationValidator noticeDataCreationValidator;

    public NoticePage(NoticeService noticeService, NoticeDataCreationValidator noticeDataCreationValidator) {
        this.noticeService = noticeService;
        this.noticeDataCreationValidator = noticeDataCreationValidator;
    }
    @InitBinder("noticeData")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(noticeDataCreationValidator);
    }

    @GetMapping("/notice")
    public String noticeCreator(Model model) {
        model.addAttribute("noticeForm", new NoticeData());
        return "NoticePage";
    }

    @PostMapping("/notice")
    public String noticeCreator(@Valid @ModelAttribute("noticeForm") NoticeData noticeForm,
                                BindingResult bindingResult,
                                HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return "NoticePage";
        }

        noticeService.makeNotice(noticeForm.getContent());
        setMessage(httpSession, "Notice successfully created!");
        return "redirect:";
    }
}
