package com.example.Bookstore.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.Bookstore.model.AppUserRepository;
import com.example.Bookstore.model.Book;
import com.example.Bookstore.model.BookstoreRepository;
import com.example.Bookstore.model.CategoryRepository;



@Controller
public class BookstoreController {
	
	@Autowired
	private BookstoreRepository repository;
	
	@Autowired
	private CategoryRepository crepository;
	
	@Autowired
	private AppUserRepository urepository;
	
	@RequestMapping("/booklist")
	public String bookList(Model model) {
		model.addAttribute("books", repository.findAll());
		return "booklist";
	}
	
	// Rest service to get all students, returns them in the JSON format
	@RequestMapping(value = "/books", method = RequestMethod.GET)
	public @ResponseBody List<Book> bookListRest() {
		return (List<Book>) repository.findAll();
	}
	
	// Note for self: the value used in url should correspond with the name of the value in the entity
	@RequestMapping(value="/book/{id}", method = RequestMethod.GET)
    public @ResponseBody Optional<Book> findBookByIdRest(@PathVariable("id") Long bookId) {	
    	return repository.findById(bookId);
    }       
	
	//Why this doesn't work @PreAuthorize("hasRole('ADMIN')")
	//And this does @PreAuthorize("hasAuthority('ADMIN')")
	// Ask about DELETE method
	@PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteBook(@PathVariable("id") Long bookId, Model model) {
    	repository.deleteById(bookId);
        return "redirect:../booklist";
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/add")
	public String addBook(Model model) {
		model.addAttribute("book", new Book());
		model.addAttribute("categories", crepository.findAll());
		return "addbook";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Book book) {
		repository.save(book);
		return "redirect:booklist";
	}
	
	@RequestMapping(value="/login")
	public String login() {
		return "login";
	}    
}