package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.rest;

import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.CouponTemplate;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.Order;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.Store;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.repository.OrderRepository;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.repository.StoreRepository;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.service.CouponNumberRangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.bson.internal.Base64;

import javax.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/backoffice")
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private CouponNumberRangeService couponNumberRangeService;

    @GetMapping(value = "/stores/add", produces = "multipart/form-data")
    public String showAddStoreForm(Store store) {
        return "add-store";
    }

    @PostMapping(value = "/stores", consumes = "multipart/form-data")
    public String addStore(@Valid Store store, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors() || bindingResult.hasFieldErrors()) {
            return "add-store";
        }

        storeRepository.save(store);

        couponNumberRangeService.generateNumbers(store, 100);
        
        model.addAttribute("stores", storeRepository.findAll());

        return "redirect:/backoffice/stores/list";
    }

    @GetMapping(value = {"", "/", "/stores/list"})
    public String allStores(Model model) {
        model.addAttribute("stores", storeRepository.findAll());

        return "list-stores";
    }

    @GetMapping("/stores/{id}")
    public String getStore(@PathVariable("id") String id, Model model) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Unable to locate store with id " + id));
        
        model.addAttribute("store", store);
        model.addAttribute("logo_full", Optional.ofNullable(store.getLogo()).map(l -> Base64.encode(l.getData())).orElse(null));
        model.addAttribute("logo_thumbnail", Optional.ofNullable(store.getThumbnail()).map(l -> Base64.encode(l.getData())).orElse(null));
        model.addAttribute("cover_photo", Optional.ofNullable(store.getCoverphoto()).map(l -> Base64.encode(l.getData())).orElse(null));
        
        return "view-store";
    }

    @GetMapping("/stores/delete/{id}")
    public String deleteStore(@PathVariable("id") String id, Model model) {
        storeRepository.deleteById(id);
        return "redirect:/backoffice/stores/list";
    }

    
    @RequestMapping({"/stores/edit/{id}"})
    public String editStoreForm(@PathVariable("id") String id, Model model) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Unable to locate store with id " + id));
        model.addAttribute("store", store);
        model.addAttribute("address", store.getAddress());
        model.addAttribute("paypalInformation", store.getPaypalInformation());
        model.addAttribute("id", id);
        return "edit-store";
    }

    @PostMapping(value = "/stores/edit/save/{id}", consumes = "multipart/form-data")
    public String saveStoreInformation(@PathVariable("id") String id, @Valid Store store, BindingResult bindingResult, Model model) {
    	
    	if (bindingResult.hasErrors() || bindingResult.hasFieldErrors()) {
            return "edit-store";
        }
    	
    	Store oldstore = storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Unable to locate store with id " + id));
    	
    	store.setId(id);
    	oldstore.getCouponTemplates().stream().forEach(template -> store.addCouponTemplate(template));
    	
    	if(store.getLogoBinaryAsString().length() == 0) {store.setLogoBinary(oldstore.getLogo());}
    	if(store.getCoverphotoBinaryAsString().length() == 0) {store.setCoverphotoBinary(oldstore.getLogo());}
    	if(store.getThumbnailBinaryAsString().length() == 0) {store.setThumbnailBinary(oldstore.getLogo());}
    	
    	storeRepository.save(store);

        model.addAttribute("store", store);
        model.addAttribute("logo_full", Optional.ofNullable(store.getLogo()).map(l -> Base64.encode(l.getData())).orElse(null));
        model.addAttribute("logo_thumbnail", Optional.ofNullable(store.getThumbnail()).map(l -> Base64.encode(l.getData())).orElse(null));
        model.addAttribute("cover_photo", Optional.ofNullable(store.getCoverphoto()).map(l -> Base64.encode(l.getData())).orElse(null));

        return "view-store";
    }
    
    @GetMapping(value = "/stores/{id}/coupontemplates/add", produces = "multipart/form-data")
    public String showAddCoupontemplateForm(@PathVariable("id") String id, Model model) {
    	
    	model.addAttribute("id", id);
    	model.addAttribute("coupontemplate", new CouponTemplate());
    	
        return "add-coupontemplate";
    }
    
    @PostMapping(value = "/stores/{id}/coupontemplates", consumes = "multipart/form-data")
    public String addCouponTemplate(@PathVariable("id") String id, @Valid CouponTemplate coupontemplate, BindingResult bindingResult, Model model) {
    	
        Store store = storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Unable to locate store with id " + id));
        store.addCouponTemplate(coupontemplate);

        storeRepository.save(store);

        model.addAttribute("store", store);
        model.addAttribute("coupontemplates", store.getCouponTemplates());

        return "redirect:/backoffice/stores/" + id + "/coupontemplates/list";
    }

    @GetMapping("/stores/{id}/coupontemplates/list")
    public String allCoupontemplates(@PathVariable("id") String id, Model model) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Unable to locate store with id " + id));

    	model.addAttribute("store", store);
        model.addAttribute("coupontemplates", store.getCouponTemplates()); 
        
        return "list-coupontemplates";
    }
    
    @GetMapping("/stores/{id}/coupontemplate/{coupontempid}")
    public String getCoupontemplate(@PathVariable("id") String id, @PathVariable("coupontempid") String coupontempid, Model model) {
    	
        Store store = storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Unable to locate store with id " + id));
        
        CouponTemplate template = store.getCouponTemplates().stream().filter(tmp -> tmp.getId().equals(coupontempid)).findAny().get();
        
        model.addAttribute("coupontemplate", template);
        model.addAttribute("coupon_image", Base64.encode(template.getCouponImage().getData()));
        model.addAttribute("coupon_image_thumbnail", Base64.encode(template.getCouponImageThumbnail().getData()));
        model.addAttribute("storeid", id);
        
        return "view-coupontemplate";
    }
    
    @GetMapping("/stores/{id}/coupontemplate/edit/{coupontempid}")
    public String editCoupontemplateForm(@PathVariable("id") String storeid, @PathVariable("coupontempid") String coupontempid, Model model) {
    	
    	Store store = storeRepository.findById(storeid).orElseThrow(() -> new IllegalArgumentException("Unable to locate store with id " + storeid));
        
        CouponTemplate template = store.getCouponTemplates().stream().filter(tmp -> tmp.getId().equals(coupontempid)).findAny().get();
        
        model.addAttribute("coupontemplate", template);
        model.addAttribute("storeid", storeid);
        
        return "edit-coupontemplate";
    }
    
    @PostMapping(value = "/stores/{id}/coupontemplate/edit/save/{coupontempid}", consumes = "multipart/form-data")
    public String saveCoupontemplateForm(@PathVariable("id") String storeid, @PathVariable("coupontempid") String coupontempid, @Valid CouponTemplate coupontemplate, BindingResult bindingResult, Model model) {
    	
    	if (bindingResult.hasErrors() || bindingResult.hasFieldErrors()) {
            return "edit-coupontemplate";
        }
    	
    	Store store = storeRepository.findById(storeid).orElseThrow(() -> new IllegalArgumentException("Unable to locate store with id " + storeid));    
        CouponTemplate template = store.getCouponTemplates().stream().filter(tmp -> tmp.getId().equals(coupontempid)).findAny().get();
        
        store.removeCouponTemplate(template);
        if(coupontemplate.getCouponImageBinaryAsString().length() == 0) { coupontemplate.setCouponImageBinary(template.getCouponImage()); }
        if(coupontemplate.getCouponImageThumbnailBinaryAsString().length() == 0) { coupontemplate.setCouponImageThumbnailBinary(template.getCouponImageThumbnail()); }
        store.addCouponTemplate(coupontemplate);
        
        storeRepository.save(store);
        
        model.addAttribute("store", store);
        model.addAttribute("coupontemplate", template); 
        
        return "redirect:/backoffice/stores/" + storeid + "/coupontemplates/list";
    }
    
    @GetMapping("/stores/{id}/coupontemplate/delete/{coupontempid}")
    public String deleteCoupontemplate(@PathVariable("id") String id, @PathVariable("coupontempid") String coupontempid, Model model) {
    	
    	Store store = storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Unable to locate store with id " + id));    
    	CouponTemplate template = store.getCouponTemplates().stream().filter(tmp -> tmp.getId().equals(coupontempid)).findAny().get();
    	
    	store.removeCouponTemplate(template);
    	
    	storeRepository.save(store);
    	
    	return "redirect:/backoffice/stores/" + id + "/coupontemplates/list";
    }
    
    @GetMapping("/stores/{id}/orders/list")
    public String allOrders(@PathVariable("id") String id, Model model) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Unable to locate store with id " + id));
        
        List<Order> orders = orderRepository.findAll().stream().filter(order -> order.getStore().getId().equals(store.getId())).collect(Collectors.toList());
        model.addAttribute("store", store);
        model.addAttribute("orders", orders);
    	
        return "list-orders";
    }
}
