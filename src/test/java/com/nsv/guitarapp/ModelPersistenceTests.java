package com.nsv.guitarapp;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.nsv.guitarapp.repository.ModelJpaRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nsv.guitarapp.model.Model;
import com.nsv.guitarapp.repository.ModelRepository;

@ContextConfiguration(locations={"classpath:com/nsv/guitarapp/applicationTests-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ModelPersistenceTests {
	@Autowired
	private ModelRepository modelRepository;
	@Autowired
	private ModelJpaRepository modelJpaRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	@Transactional
	public void testSaveAndGetAndDelete() throws Exception {
		Model m = new Model();
		m.setFrets(10);
		m.setName("Test Model");
		m.setPrice(BigDecimal.valueOf(55L));
		m.setWoodType("Maple");
		m.setYearFirstMade(new Date());
		m = modelRepository.create(m);
		
		// clear the persistence context so we don't return the previously cached location object
		// this is a test only thing and normally doesn't need to be done in prod code
		entityManager.clear();

		Model otherModel = modelRepository.find(m.getId());
		assertEquals("Test Model", otherModel.getName());
		assertEquals(10, otherModel.getFrets());
		
		//delete BC location now
		modelRepository.delete(otherModel);
	}

	@Test
	public void testGetModelsInPriceRange() throws Exception {
			List<Model> mods = modelRepository.getModelsInPriceRange(BigDecimal.valueOf(1000L), BigDecimal.valueOf(2000L));
		assertEquals(4, mods.size());
	}

	@Test
	public void testGetModelsByPriceRangeAndWoodType() throws Exception {
		Page<Model> mods = modelRepository.getModelsByPriceRangeAndWoodType(BigDecimal.valueOf(1000L), BigDecimal.valueOf(2000L), "Maple");
		assertEquals(2, mods.getSize());
	}

	@Test
	public void testGetModelsByType() throws Exception {
		List<Model> mods = modelRepository.getModelsByType("Electric");
		assertEquals(4, mods.size());

		modelJpaRepository.aCustomMethod();
	}

	@Test
	public void testGetModelsByTypes() throws Exception {
		List<String> modelTypeNames = new ArrayList<>();
		modelTypeNames.add("Electric");
		modelTypeNames.add("Bass");

		List<Model> mods = modelJpaRepository.findByModelTypeNameIn(modelTypeNames);

		mods.forEach(model -> {
			Assert.assertTrue(model.getModelType().getName().equals("Electric") || model.getModelType().getName().equals("Bass"));
		});

	}
}
