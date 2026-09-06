package com.medbill.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.medbill.entity.Category;
import com.medbill.entity.Company;
import com.medbill.repository.CategoryRepository;
import com.medbill.repository.CompanyRepository;
import com.medbill.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CompanyRepository companyRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CompanyRepository companyRepository) {
        this.categoryRepository = categoryRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Category> getAllCategories(Long companyId) {
        ensureDefaultCategoriesSeeded(companyId);
        if (companyId != null) {
            return categoryRepository.findByCompanyIdOrderByPositionAsc(companyId);
        }
        return categoryRepository.findAllByOrderByPositionAsc();
    }

    @Override
    public List<Category> getActiveCategories(Long companyId) {
        ensureDefaultCategoriesSeeded(companyId);
        if (companyId != null) {
            return categoryRepository.findByCompanyIdAndStatusOrderByPositionAsc(companyId, "ACTIVE");
        }
        return categoryRepository.findByStatusOrderByPositionAsc("ACTIVE");
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    @Override
    public Category createCategory(Category category, Long companyId) {
        Company company = resolveCompany(companyId);
        category.setCompany(company);

        if (category.getPosition() == null || category.getPosition() <= 0) {
            long count = companyId != null ? categoryRepository.findByCompanyIdOrderByPositionAsc(companyId).size() : categoryRepository.count();
            category.setPosition((int) count + 1);
        }

        if (category.getCode() == null || category.getCode().trim().isEmpty()) {
            String cleanName = category.getName().replaceAll("[^a-zA-Z]", "").toUpperCase();
            category.setCode(cleanName.length() >= 3 ? cleanName.substring(0, 3) : cleanName);
        } else {
            category.setCode(category.getCode().trim().toUpperCase());
        }

        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, Category updated) {
        Category existing = getCategoryById(id);

        if (updated.getName() != null && !updated.getName().trim().isEmpty()) {
            existing.setName(updated.getName().trim());
        }
        if (updated.getCode() != null && !updated.getCode().trim().isEmpty()) {
            existing.setCode(updated.getCode().trim().toUpperCase());
        }
        if (updated.getDescription() != null) {
            existing.setDescription(updated.getDescription().trim());
        }
        if (updated.getPosition() != null) {
            existing.setPosition(updated.getPosition());
        }
        if (updated.getStatus() != null && !updated.getStatus().trim().isEmpty()) {
            existing.setStatus(updated.getStatus().trim().toUpperCase());
        }

        return categoryRepository.save(existing);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    // =========================================================================
    // SEED INITIAL PHARMACY CATEGORIES (Tablets, Syrups, Injections, etc.)
    // =========================================================================
    private synchronized void ensureDefaultCategoriesSeeded(Long companyId) {
        Company company = resolveCompany(companyId);
        Long targetCid = company != null ? company.getId() : null;

        List<Category> existing = targetCid != null
                ? categoryRepository.findByCompanyIdOrderByPositionAsc(targetCid)
                : categoryRepository.findAllByOrderByPositionAsc();

        if (existing.isEmpty()) {
            Category[] defaults = {
                new Category("Tablets", "TAB", "Oral solid dosage forms and chewable tablets", 1, "ACTIVE", company),
                new Category("Syrups", "SYR", "Liquid formulations, suspensions and cough syrups", 2, "ACTIVE", company),
                new Category("Injections", "INJ", "Injectable vials, ampoules and intravenous solutions", 3, "ACTIVE", company),
                new Category("Antibiotics", "ANT", "Broad-spectrum antibacterial and antimicrobial drugs", 4, "ACTIVE", company),
                new Category("Vitamins", "VIT", "Multivitamins, minerals and nutritional dietary supplements", 5, "ACTIVE", company),
                new Category("First Aid", "FA", "Bandages, antiseptics, cotton and emergency care supplies", 6, "ACTIVE", company),
                new Category("General", "GEN", "General healthcare products, personal hygiene and OTC goods", 7, "ACTIVE", company)
            };

            for (Category cat : defaults) {
                categoryRepository.save(cat);
            }
        }
    }

    private Company resolveCompany(Long companyId) {
        if (companyId != null) {
            return companyRepository.findById(companyId).orElse(null);
        }
        List<Company> companies = companyRepository.findAll();
        return !companies.isEmpty() ? companies.get(0) : null;
    }
}

