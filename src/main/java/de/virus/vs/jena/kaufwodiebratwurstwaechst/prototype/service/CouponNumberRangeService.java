package de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.service;

import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.CouponNumberRange;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.entity.Store;
import de.virus.vs.jena.kaufwodiebratwurstwaechst.prototype.repository.CouponNumberRangeRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Scope("singleton")
public class CouponNumberRangeService {

    @Autowired
    private CouponNumberRangeRepository numberRangeRepository;

    synchronized public void generateNumbers(Store store, int numberOfCouponNumbersToBeGenerated) {
        System.out.println("generating coupons");
        // get current maximum running number
        Optional<CouponNumberRange> firstByOrderByRunningNumberDesc = numberRangeRepository.findFirstByStoreOrderByRunningNumberDesc(store.getId());

        int currentlyHighestRunningNumber = firstByOrderByRunningNumberDesc.map(CouponNumberRange::getRunningNumber).orElse(0);
        System.out.println(currentlyHighestRunningNumber);

        // generate random part - take all currently generated numbers into account?
        for (int i = 0; i < numberOfCouponNumbersToBeGenerated; i++) {

            CouponNumberRange couponNumberRange = new CouponNumberRange();
            couponNumberRange.setStore(store);
            couponNumberRange.setRunningNumber(currentlyHighestRunningNumber + i + 1);
            couponNumberRange.setUniquePart(generateRandomPart());
            couponNumberRange.setUsed(false);

            numberRangeRepository.save(couponNumberRange);
        }
    }

    private String generateRandomPart() {
        return RandomStringUtils.randomAlphanumeric(8);
    }


    synchronized public CouponNumberRange fetchCouponNumberAndMarkAsUsed(Store store) {
        // atomic -> fetch unsused and set to used
        CouponNumberRange couponNumberRange = numberRangeRepository.getUnusedAndMarkAsUsed(store.getId());
        if(couponNumberRange == null) {
            // no number range present yet or no unused left
            // create new one and try again
            generateNumbers(store, 100);
            couponNumberRange = numberRangeRepository.getUnusedAndMarkAsUsed(store.getId());
        }

        long numberOfUnusedCoupons = numberRangeRepository.countUnusedDocumentsForStoreId(store.getId());

        // if remaining number of coupons <= 5 -> generate new set
        if (numberOfUnusedCoupons <= 5) {
            generateNumbers(store, 100);
        }

        return couponNumberRange;
    }

}
