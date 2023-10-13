package com.unext.capstone.promotionsandoffers.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unext.capstone.promotionsandoffers.exception.DiscountNotExistsException;
import com.unext.capstone.promotionsandoffers.exception.PromotionAlreadyExistsException;
import com.unext.capstone.promotionsandoffers.exception.PromotionInvalidException;
import com.unext.capstone.promotionsandoffers.exception.PromotionNotExistsException;
import com.unext.capstone.promotionsandoffers.model.Discount;
import com.unext.capstone.promotionsandoffers.model.Promotion;
import com.unext.capstone.promotionsandoffers.repository.PromotionRepo;

@Service
public class PromotionService {
	@Autowired
	PromotionRepo repo;
	
	public Promotion insertPromotionDetails(Promotion promo) throws PromotionAlreadyExistsException, PromotionInvalidException ,Exception
	{
		//Check if the Promotion already exists in the database and throw exception
		Optional<Promotion> opt=this.repo.findById(promo.getPromoCode());
		if(opt.isPresent()) {
			throw new PromotionAlreadyExistsException();
		}
		// Throw exception when percentage or number of promo's are invalid
		if(promo.getPromoPercentage()<0 || promo.getNbrOfPromos()<0 ||
				promo.getPromoPercentage()>99 || promo.getNbrOfPromos()>999 )
			throw new PromotionInvalidException();
		//Save and return promotion collection record
		repo.save(promo);
		return promo;
	}
	
	public Promotion updatePromotionDetails(Promotion promo) throws PromotionInvalidException , Exception
	{	
		// Throw exception when percentage or number of promo's are invalid
		if(promo.getPromoPercentage()<0 || promo.getNbrOfPromos()<0 ||
			promo.getPromoPercentage()>99 || promo.getNbrOfPromos()>999 )
			throw new PromotionInvalidException();
		
		Optional<Promotion> opt=this.repo.findById(promo.getPromoCode());
		//whenever no values for the fields are passed in the pay-load, data will be fetched from Promotion collection 
		if(opt.isPresent()) {
			if(promo.getStartDate()==null)
				promo.setStartDate(opt.get().getStartDate());
			if(promo.getEndDate()==null)
				promo.setEndDate(opt.get().getEndDate());
			if(promo.getCreatedDate()==null)
				promo.setCreatedDate(opt.get().getCreatedDate());
			if(promo.getPromoPercentage()==0)
				promo.setPromoPercentage(opt.get().getPromoPercentage());
			if(promo.getNbrOfPromos()==0)
				promo.setNbrOfPromos(opt.get().getNbrOfPromos());
		}
		//Save and return promotion collection record
		repo.save(promo);
		return promo;
	}
	
	public Promotion deletePromotionDetails(String promoCode) throws PromotionNotExistsException ,Exception
	{
		//Check if the promotion exists in the database and throw exception
		Optional<Promotion> opt=this.repo.findById(promoCode);
		if(!opt.isPresent()) {
			throw new PromotionNotExistsException();
		}
		
		//delete and return Promotion collection record
		repo.delete(opt.get());
		return opt.get();
	}
}

