package com.saurav.BlinkDeliver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> subCategories;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = true) // Brand might be optional for sub-categories or root categories
                                                    // depending on logic, keeping it nullable for flexibility or strict
                                                    // based on requirements. User didn't specify, but usually
                                                    // categories are generic. Let's keep it as is or check if user
                                                    // wants it. User didn't mention brand in new Category reference.
                                                    // I'll keep it but make it nullable if needed, or just leave it.
                                                    // The user's reference code DOES NOT have brand. I will REMOVE
                                                    // brand to match user's reference code.
    // private Brand brand; // Removing Brand as per user reference code

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Helper methods
    public void addSubCategory(Category child) {
        child.setParent(this);
        if (this.subCategories == null) {
            this.subCategories = new java.util.ArrayList<>();
        }
        this.subCategories.add(child);
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return subCategories == null || subCategories.isEmpty();
    }
}