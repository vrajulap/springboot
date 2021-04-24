package com.nrg.userms.controller;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.nrg.userms.model.User;
import com.nrg.userms.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;

	

	@GetMapping("/users")
	public ResponseEntity<?> getAllUsers() {
		List<User> users = userService.getAllUsers();
		return users.size() < 1 ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<?> findUser(@PathVariable Long userId) {
		Optional<User> user = null;
		try {
			user = userService.findUser(userId);

			if (user.isPresent()) {
				return new ResponseEntity<User>(user.get(), HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Unable to Find User with Id " + userId + ", reason " + e,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("No User with userId " + userId + " Exists", HttpStatus.NOT_FOUND);
	}

	@PostMapping("/user")
	public ResponseEntity<?> createUser(@Valid @RequestBody User user, UriComponentsBuilder ucBuilder)
			throws URISyntaxException {

		HttpHeaders headers = null;
		try {
			user = userService.saveOrUpdate(user);
			headers = new HttpHeaders();
			headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());

		} catch (Exception e) {
			return new ResponseEntity<String>("Unable to create user  with details :  " + user + ", reason " + e,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<User>(user, headers, HttpStatus.CREATED);
	}

	@PutMapping("/user/{userId}")
	public ResponseEntity<?> updateUser(@Valid @RequestBody User user, @PathVariable Long userId) {
		Optional<User> curUser = null;
		try {
			curUser = userService.findUser(userId);
			if (curUser.isPresent()) {

				User upUser = curUser.get();
				upUser.setName(user.getName());
				upUser.setEmail(user.getEmail());

				upUser = userService.saveOrUpdate(upUser);
				return new ResponseEntity<User>(upUser, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("Unable to update User with user Id  " + userId + ", reason " + e,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("No user with Id " + userId + " Exists", HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/user/{userId}")
	ResponseEntity<?> deleteUser(@PathVariable Long userId) {
		Optional<User> curUser = null;
		try {
			curUser = userService.findUser(userId);
			if (curUser.isPresent()) {
				userService.deleteUser(userId);
				return new ResponseEntity<User>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("Unable to delete user with User Id " + userId + ", reason " + e,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("No user with Id " + userId + " Exists", HttpStatus.OK);
	}

}
