package ru.itmo.wp.form.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itmo.wp.form.CommentData;
import ru.itmo.wp.service.JwtService;
@Component
public class CommentDataCreationValidator implements Validator {
    private final JwtService jwtService;

    public CommentDataCreationValidator(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CommentData.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasErrors()) {
            CommentData commentForm = (CommentData) target;
            if (jwtService.find(commentForm.getJwt()) == null) {
                errors.rejectValue("user", "no-access", "No access");
            }
        }
    }
}
