package org.luan.validation.constraintvalidation;

import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.luan.validation.NotEmptyList;

public class NotEmptyListValidator implements ConstraintValidator<NotEmptyList, List> {

  @Override
  public void initialize(NotEmptyList constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(List list, ConstraintValidatorContext constraintValidatorContext) {
    //Se não passar nesse válido customizado, a lista é inválida
    return list!= null && !list.isEmpty();
  }
}
