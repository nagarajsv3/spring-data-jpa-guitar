package com.nsv.guitarapp;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.nsv.guitarapp.repository.LocationJpaRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nsv.guitarapp.model.Location;
import com.nsv.guitarapp.repository.LocationRepository;

@ContextConfiguration(locations={"classpath:com/nsv/guitarapp/applicationTests-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class LocationPersistenceTests {
	@Autowired
	private LocationJpaRepository locationJpaRepository;


	@PersistenceContext
	private EntityManager entityManager;

	@Test
	public void testJpafindAll(){
		List<Location> all = locationJpaRepository.findAll();
		Assert.assertNotNull(all);
	}

	@Test
	public void testJpaFindAnd(){
		List<Location> locations = locationJpaRepository.findByStateAndCountry("Utah","United States");
		Assert.assertEquals("Utah",locations.get(0).getState());
	}

	@Test
	public void testJpaFindIsEquals(){
		List<Location> locations = locationJpaRepository.findByStateIsOrCountryEquals("Utah","United States1");
		Assert.assertEquals("Utah",locations.get(0).getState());
	}

	@Test
	public void testJpaFindIsNot(){
		List<Location> locations = locationJpaRepository.findByStateIsOrCountryNot("Utah","United States");
		Assert.assertEquals("Utah",locations.get(0).getState());
	}

	@Test
	public void testJpaFindNot(){
		List<Location> locations = locationJpaRepository.findByStateNot("Utah");
		Assert.assertNotSame("Utah",locations.get(0).getState());
	}

	@Test
	public void testJpaFindOr(){
		List<Location> locations = locationJpaRepository.findByStateOrCountry("Utah","United States123");
		Assert.assertEquals("Utah",locations.get(0).getState());
	}

	@Test
	@Transactional
	public void testSaveAndGetAndDelete() throws Exception {
		Location location = new Location();
		location.setCountry("Canada");
		location.setState("British Columbia");
		location = locationJpaRepository.saveAndFlush(location);
		
		// clear the persistence context so we don't return the previously cached location object
		// this is a test only thing and normally doesn't need to be done in prod code
		entityManager.clear();

		Location otherLocation = locationJpaRepository.findOne(location.getId());
		assertEquals("Canada", otherLocation.getCountry());
		assertEquals("British Columbia", otherLocation.getState());
		
		//delete BC location now
		locationJpaRepository.delete(otherLocation);
	}

	@Test
	public void testFindWithLike() throws Exception {
		List<Location> locs = locationJpaRepository.findByStateIgnoreCaseLike("new%");
		assertEquals(4, locs.size());
	}

	@Test
	public void testFindStartingWith() throws Exception {
		List<Location> locs = locationJpaRepository.findByStateStartingWith("New");
		assertEquals(4, locs.size());
	}

	@Test
	public void testFindNotLike() throws Exception {
		List<Location> locs = locationJpaRepository.findByStateNotLike("New%");
		assertEquals(46, locs.size());
	}

	@Test
	public void testFindNotLikeOrderByAsc() throws Exception {
		List<Location> locs = locationJpaRepository.findByStateNotLikeOrderByCountryAsc("New%");
		assertEquals(46, locs.size());

		locs.forEach(location -> {
			System.out.println(location.getState());
		});
	}

	@Test
	public void testFirst() throws Exception {
		Location locs = locationJpaRepository.findFirstByStateIgnoreCaseLike("a%");
		assertEquals("Alabama", locs.getState());

	}

/*	@Test
	public void testDistinct() throws Exception {
		List<Location> locs = locationJpaRepository.findByStateIgnoreCaseLike("Alabama");
		assertEquals("FirstTest",2, locs.size());

		locs.forEach(location -> {
			System.out.println(location.getState());
		});

		System.out.println("*************************");
		List<Location> locsdis = locationJpaRepository.findDistinctByStateIgnoreCaseLike("Alabama");
		assertEquals("DistinctTest",1, locsdis.size());

		locsdis.forEach(location -> {
			System.out.println(location.getState());
		});
	}*/

	@Test
	@Transactional  //note this is needed because we will get a lazy load exception unless we are in a tx
	public void testFindWithChildren() throws Exception {
		Location arizona = locationJpaRepository.findOne(3L);
		assertEquals("United States", arizona.getCountry());
		assertEquals("Arizona", arizona.getState());
		
		assertEquals(1, arizona.getManufacturers().size());
		
		assertEquals("Fender Musical Instruments Corporation", arizona.getManufacturers().get(0).getName());
	}
}
