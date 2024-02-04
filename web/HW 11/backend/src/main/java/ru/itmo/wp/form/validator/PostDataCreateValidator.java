package ru.itmo.wp.form.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itmo.wp.form.PostData;
import ru.itmo.wp.service.JwtService;
@Component
public class PostDataCreateValidator implements Validator {
    private final JwtService jwtService;

    public PostDataCreateValidator(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return PostData.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasErrors()) {
            PostData postForm = (PostData) target;
            if (jwtService.find(postForm.getJwt()) == null) {
                errors.rejectValue("user", "no-access", "No access");
            }
        }
    }
}
