package com.example.demo.controller;

import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.JwtTokenProvider;
import com.example.demo.config.SpringSecurityConfig;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.BookAlreadyRegistered;
import com.example.demo.exception.BookNotFound;
import com.example.demo.exception.UserAlreadyRegistred;
import com.example.demo.model.BookModel;
import com.example.demo.model.UserData;
import com.example.demo.model.UserModel;
import com.example.demo.repository.IssueRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserController {

	@Autowired
	private UserService service;
	@Autowired
	SpringSecurityConfig config;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenProvider tokenProvider;
	@Autowired
	private UserRepository userRepo;

	@PostMapping(value = "/user/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> authenticate(@RequestBody UserData user) {
		log.info("UserResourceImpl : authenticate");
		JSONObject jsonObject = new JSONObject();
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
			if (authentication.isAuthenticated()) {
				String email = user.getUsername();
				UserEntity userEntity = userRepo.findByEmail(email);

				jsonObject.put("name", userEntity.getName());
				jsonObject.put("email", authentication.getName());
				jsonObject.put("roles", authentication.getAuthorities());
				jsonObject.put("token",
						tokenProvider.createToken(email, Arrays.asList(userEntity.getRoles().split(","))));

				return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.OK);
			}
		} catch (JSONException e) {
			try {
				jsonObject.put("exception", e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.UNAUTHORIZED);
		}
		return null;
	}

	@PostMapping("/user/register")
	public ResponseEntity<?> createUserWithoutAdmin(@RequestBody UserModel user) throws UserAlreadyRegistred {

		String pwd = user.getPassword();
		String encryptPsw = config.passwordEncoder().encode(pwd);
		user.setPassword(encryptPsw);

		return new ResponseEntity<UserModel>(service.createUser(user), HttpStatus.OK);
	}

	@PostMapping("/add/book")
	public ResponseEntity<?> addBooks(@RequestBody BookModel model) throws BookAlreadyRegistered {

		return new ResponseEntity<BookModel>(service.addBook(model), HttpStatus.OK);
	}

	@DeleteMapping("/delete/book")
	public ResponseEntity<?> deleteBook(@RequestParam(value = "id") int id) {
		service.deleteBook(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/view/book")
	public ResponseEntity<?> viewBooks() {
		return new ResponseEntity<List<BookModel>>(service.viewBooks(), HttpStatus.OK);
	}

	@GetMapping("/search/book")
	public ResponseEntity<?> searchBook(@RequestParam(value = "id") int id) throws BookNotFound {
		return new ResponseEntity<BookModel>(service.searchBook(id), HttpStatus.OK);
	}

	@GetMapping("/issued/books")
	public ResponseEntity<?> issueBooks() {
		return new ResponseEntity<List<BookModel>>(service.issueBooks(), HttpStatus.OK);
	}
}
