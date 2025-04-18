package com.h5radar.radar.domain.tenant;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.h5radar.radar.domain.FlashMessages;
import com.h5radar.radar.domain.ModelError;
import com.h5radar.radar.domain.ValidationException;

@Controller
@RequestMapping("/settings/tenants")
@RequiredArgsConstructor
public class DeleteTenantCfgController {

  private final TenantService tenantService;
  private final MessageSource messageSource;

  @GetMapping("")
  public ModelAndView index(@Valid TenantFilter tenantFilter, BindingResult bindingResult,
                            @RequestParam(defaultValue = "${application.paging.page}") int page,
                            @RequestParam(defaultValue = "${application.paging.size}") int size,
                            @RequestParam(defaultValue = "title,asc") String[] sort) {
    Sort.Direction direction = sort[1].equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Sort.Order order = new Sort.Order(direction, sort[0]);

    ModelAndView modelAndView = new ModelAndView("settings/tenants/index");
    Page<TenantDto> tenantDtoPage = tenantService.findAll(tenantFilter, PageRequest.of(page - 1, size, Sort.by(order)));
    modelAndView.addObject("tenantDtoPage", tenantDtoPage);
    modelAndView.addObject("tenantFilter", tenantFilter);
    modelAndView.addObject("currentPage", tenantDtoPage.getNumber() + 1);
    modelAndView.addObject("pageSize", size);
    modelAndView.addObject("sortField", sort[0]);
    modelAndView.addObject("sortDirection", sort[1]);
    modelAndView.addObject("reverseSortDirection", sort[1].equals("asc") ? "desc" : "asc");

    int totalPages = tenantDtoPage.getTotalPages();
    if (totalPages > 0) {
      List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
      modelAndView.addObject("pageNumbers", pageNumbers);
    }
    return modelAndView;
  }

  @GetMapping("/show/{id}")
  public ModelAndView show(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
    Optional<TenantDto> tenantRecord = tenantService.findById(id);
    if (tenantRecord.isPresent()) {
      ModelAndView modelAndView = new ModelAndView("settings/tenants/show");
      modelAndView.addObject("tenantDto", tenantRecord.get());
      return modelAndView;
    } else {
      redirectAttributes.addFlashAttribute(FlashMessages.ERROR,
          messageSource.getMessage("tenant.error.invalid_id", null,
              LocaleContextHolder.getLocale()));
      return new ModelAndView("redirect:/settings/tenants");
    }
  }

  @GetMapping("/add")
  public ModelAndView add() {
    ModelAndView modelAndView = new ModelAndView("settings/tenants/add");
    modelAndView.addObject("tenantDto", new TenantDto());
    return modelAndView;
  }

  @PostMapping(value = "/create")
  public ModelAndView create(@Valid TenantDto tenantDto, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
    try {
      tenantService.save(tenantDto);
      redirectAttributes.addFlashAttribute(FlashMessages.INFO,
          messageSource.getMessage("tenant.info.created", null,
              LocaleContextHolder.getLocale()));
      return new ModelAndView("redirect:/settings/tenants");
    } catch (ValidationException e) {
      // Add errors to fields and global
      for (ModelError modelError : e.getModelErrorList()) {
        if (modelError.getField() == null || modelError.getField().isEmpty() || modelError.getField().isBlank()) {
          bindingResult.reject(modelError.getErrorCode(), modelError.getErrorMessage());
        } else {
          bindingResult.rejectValue(modelError.getField(), modelError.getErrorCode(), modelError.getErrorMessage());
        }
      }

      // Show form again
      ModelAndView modelAndView = new ModelAndView("settings/tenants/add");
      modelAndView.addObject("tenantDto", tenantDto);
      return modelAndView;
    }
  }

  @GetMapping(value = "/edit/{id}")
  public ModelAndView edit(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
    Optional<TenantDto> tenantDto = tenantService.findById(id);
    if (tenantDto.isPresent()) {
      ModelAndView modelAndView = new ModelAndView("settings/tenants/edit");
      modelAndView.addObject("tenantDto", tenantDto.get());
      return modelAndView;
    } else {
      redirectAttributes.addFlashAttribute(FlashMessages.ERROR,
          messageSource.getMessage("tenant.error.invalid_id", null,
              LocaleContextHolder.getLocale()));
      return new ModelAndView("redirect:/settings/tenants");
    }
  }

  @PostMapping("/update")
  public ModelAndView update(@Valid TenantDto tenantDto,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes) {
    try {
      tenantService.save(tenantDto);
      redirectAttributes.addFlashAttribute(FlashMessages.INFO,
          messageSource.getMessage("tenant.info.updated", null,
              LocaleContextHolder.getLocale()));
      return new ModelAndView("redirect:/settings/tenants");
    } catch (ValidationException e) {
      // Add errors to fields and global
      for (ModelError modelError : e.getModelErrorList()) {
        if (modelError.getField() == null || modelError.getField().isEmpty() || modelError.getField().isBlank()) {
          bindingResult.reject(modelError.getErrorCode(), modelError.getErrorMessage());
        } else {
          bindingResult.rejectValue(modelError.getField(), modelError.getErrorCode(), modelError.getErrorMessage());
        }
      }

      // Show form again
      ModelAndView modelAndView = new ModelAndView("settings/tenants/edit");
      modelAndView.addObject("tenantDto", tenantDto);
      return modelAndView;
    }
  }

  @GetMapping(value = "/delete/{id}")
  public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
    tenantService.deleteById(id);
    redirectAttributes.addFlashAttribute(FlashMessages.INFO,
        messageSource.getMessage("tenant.info.deleted", null,
            LocaleContextHolder.getLocale()));
    return "redirect:/settings/tenants";
  }
}
