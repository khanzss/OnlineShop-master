package com.gmail.merikbest2015.ecommerce.domain;


import com.gmail.merikbest2015.ecommerce.domain.Category;
import javax.persistence.*;

//import jakarta.persistence.*;
//
@Entity
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String icon;
    private int displayOrder;

    private boolean visible; // Trường xác định trạng thái hiển thị

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Getters và Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

