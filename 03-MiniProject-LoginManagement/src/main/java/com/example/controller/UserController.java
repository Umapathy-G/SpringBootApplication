package com.example.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.User;
import com.example.service.UserService;
import com.example.util.EmailSender;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private EmailSender emailSender;

	@GetMapping("/")
	public String loginPage(Model model) {
		model.addAttribute("UserData", new User());
		return "login";
	}

	@GetMapping("/register")
	public String registerPage(Model model) {
		List<String> countries = userService.getCountries();
		model.addAttribute("countryList", countries);
		model.addAttribute("UserData", new User());
		return "register";
	}

	@GetMapping("/forgotPassword")
	public String forgotPasswordPage(Model model) {
		model.addAttribute("UserData", new User());
		return "forgotpassword";
	}

	@PostMapping("/resendPassword")
	public String resendPassword(@ModelAttribute("userData") User user, RedirectAttributes redirectAttributes) {
		boolean validateEmail = userService.validateEmail(user.getEmail());
		if (validateEmail) {
			User findUserByMail = userService.findUserByMail(user.getEmail());
			String sendEmail = emailSender.sendEmail(findUserByMail,"resend");
			redirectAttributes.addFlashAttribute("successMsg", sendEmail);
		}
		else {
			redirectAttributes.addFlashAttribute("errorMsg", "Invalid Mail-id");
		}
		return "redirect:/forgotPassword";
	}

	@GetMapping("/validate-email")
	@ResponseBody
	public boolean validateEmail(@RequestParam("email") String email) {
		boolean validateEmail = userService.validateEmail(email);
		System.out.println("Email id exists: " + validateEmail);
		return validateEmail;
	}

	@GetMapping("/get-data")
	@ResponseBody
	public Map<String, Map<String, List<String>>> getDropdownData() {
		return userService.getDropdownData();
	}

	@PostMapping("/registerUser")
	public String registerUser(@ModelAttribute("userData") User user, RedirectAttributes redirectAttributes) {
		String registerUser = userService.registerUser(user);
		String sendEmail = emailSender.sendEmail(user,"send");
		System.out.println("Record: " + registerUser);
		System.out.println("Mail: " + sendEmail);
		redirectAttributes.addFlashAttribute("registerMsg", registerUser);
		redirectAttributes.addFlashAttribute("emailMsg", sendEmail);
		return "redirect:/register";
	}

	@GetMapping("/unlock/{id}")
	public String unlockPage(@PathVariable("id") Integer id, Model model) {
		User user = userService.findUserById(id);
		System.out.println(user);
		model.addAttribute("IsLocked", user.getIsLocked());
		model.addAttribute("userData", user);
		return "unlock";
	}

	@PostMapping("/unlockUser")
	public String unlockUser(@ModelAttribute("userData") User user, Model model) {
		User dbUser = userService.findUserById(user.getId());
		dbUser.setNewPassword(user.getNewPassword());
		dbUser.setConfirmPassword(user.getConfirmPassword());
		userService.registerUser(dbUser);
		model.addAttribute("UserData", new User());
		return "redirect:/unlock-success?IsLocked=" + dbUser.getIsLocked();
	}

	@GetMapping("/unlock-success")
	public String unlockSuccess(Model model, @RequestParam("IsLocked") String IsLocked) {
		model.addAttribute("IsLocked", IsLocked);
		model.addAttribute("userData", new User());
		return "unlock";
	}

	@GetMapping("/validate-temp-password")
	@ResponseBody
	public boolean validateTempPassword(@RequestParam("tempPassword") String tempPassword,
			@RequestParam("email") String email) {
		User findUserByMail = userService.findUserByMail(email);
		if (tempPassword.equalsIgnoreCase(findUserByMail.getTempPassword())) {
			System.out.println("Temporary Password matched");
			return true;
		}
		System.out.println("Temporary Password is not matched");
		return false;
	}

	@PostMapping("/loginUser")
	public String logInUser(@ModelAttribute("UserData") User user, Model model) {
		boolean validateEmail = userService.validateEmail(user.getEmail());
		User findUserByMail = userService.findUserByMail(user.getEmail());
		Optional<User> checkLoginUser = userService.checkLoginUser(user.getEmail(), user.getConfirmPassword());

		if (validateEmail) {
			if (findUserByMail != null && ("Y").equalsIgnoreCase(findUserByMail.getIsLocked())) {
				model.addAttribute("msg", "*Account is Locked");
				return "login";
			}
		}

		if (!checkLoginUser.isPresent()) {
			model.addAttribute("msg", "*Invalid Credentials");
			return "login";
		}
		User user1 = checkLoginUser.get();
		model.addAttribute("msg", user1.getFirstName() + " " + user1.getLastName());
		return "dashboard";
	}

}
