package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Book;
import com.example.demo.entity.IssuedBook;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.BookAlreadyRegistered;
import com.example.demo.exception.BookNotFound;
import com.example.demo.exception.UserAlreadyRegistred;
import com.example.demo.model.BookModel;
import com.example.demo.model.UserModel;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.IssueRepository;
import com.example.demo.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	@Autowired
	private UserRepository repository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private IssueRepository issueRepo;

	public UserModel createUser(UserModel user) throws UserAlreadyRegistred {

		UserEntity entity = new UserEntity();
		entity.setEmail(user.getEmail());
		entity.setName(user.getName());
		entity.setPassword(user.getPassword());
		entity.setActive(false);
		entity.setContact(user.getContact());
		entity.setRoles(user.getRoles());

		// checking the register email is already present in DB or not.
		String email = repository.findByEmailId(user.getEmail());
		if (email == null) {
			entity = repository.save(entity);
			BeanUtils.copyProperties(entity, user);
		} else {
			throw new UserAlreadyRegistred("user already registered");
		}

		return user;

	}

	public BookModel addBook(BookModel model) throws BookAlreadyRegistered {

		String name = bookRepository.FindByBookName(model.getName());
		if (!(name.equals(model.getName()))) {
			Book book = new Book();
			book.setAuthor(model.getAuthor());
			book.setName(model.getName());
			book.setPrice(model.getPrice());
			book = bookRepository.save(book);
			BeanUtils.copyProperties(book, model);
		} else {
			throw new BookAlreadyRegistered("book already registred");
		}
		return model;

	}

	public void deleteBook(int id) {

		Optional<Book> bookData = bookRepository.findById(id);
		if (bookData.isPresent()) {
			bookRepository.deleteById(id);
		}
	}

	public List<BookModel> viewBooks() {
		try {
			List<Book> listBook = bookRepository.findAll();
			List<BookModel> modelList = new ArrayList<BookModel>();
			listBook.forEach(data -> {
				BookModel model = new BookModel();
				model.setBookId(data.getBookId());
				model.setAuthor(data.getAuthor());
				model.setPrice(data.getPrice());
				model.setName(data.getName());
				modelList.add(model);
			});

			return modelList;
		} catch (Exception e) {
			log.error("Getting exception while retieving the records", e);
		}
		return null;
	}

	public BookModel searchBook(int id) throws BookNotFound {

		BookModel model = new BookModel();

		Optional<Book> bookData = bookRepository.findById(id);
		if (bookData.isPresent()) {
			Book entity = bookData.get();
			BeanUtils.copyProperties(entity, model);

		} else {
			throw new BookNotFound("book not found");
		}
		return model;
	}

	public List<BookModel> issueBooks() {
		try {
			List<IssuedBook> issueBooks = issueRepo.findAll();
			List<BookModel> modelList = new ArrayList<BookModel>();
			issueBooks.forEach(data -> {
				BookModel model = new BookModel();
				model.setBookId(data.getBookId());
				model.setAuthor(data.getAuthor());
				model.setPrice(data.getPrice());
				model.setName(data.getName());
				modelList.add(model);
			});

			return modelList;
		} catch (Exception e) {
			log.error("Getting exception while retieving the records", e);
		}
		return null;

	}
}
