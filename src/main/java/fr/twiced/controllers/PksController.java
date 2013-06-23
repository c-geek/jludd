package fr.twiced.controllers;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import fr.twiced.entities.PublicKey;
import fr.twiced.service.PublicKeyService;

@Controller
@RequestMapping("/pks")
public class PksController {
	
	@Autowired
	private PublicKeyService pubkeyService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView addGet(){
		return new ModelAndView("pks/add");
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "text/plain")
	@ResponseBody
	public String addPost(@RequestParam String pgpMessage){
		pubkeyService.add(pgpMessage);
		return lookup();
	}
	
	@RequestMapping(value = {"/lookup"}, produces = "text/plain")
	@ResponseBody
	public String lookup(){
		Collection<PublicKey> pubkeys = pubkeyService.findAll();
		StringBuffer buff = new StringBuffer("Keys:\n");
		Iterator<PublicKey> keys = pubkeys.iterator();
		while(keys.hasNext()){
			PublicKey k = keys.next();
			buff.append(k.getFingerprintFormatted() + " " + k.getName() + " " + k.getComment() + " " + k.getEmail() +"\n");
		}
		return buff.toString();
	}
}
