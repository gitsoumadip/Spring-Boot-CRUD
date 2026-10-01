package com.example.helloworld.Controller.Budget;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {

    @GetMapping({ "", "/" })
    public String getBudget() {
        System.out.println("budget controller accessed");
        return "Budget";

    }

    @GetMapping("/create")
    public String createBudget() {
        System.out.println("create budget accessed");
        return "Create Budget";
    }

    @GetMapping("/update")
    public String updateBudget() {
        System.out.println("update budget accessed");
        return "Update Budget";
    }

    @GetMapping("/delete")
    public String deleteBudget() {
        System.out.println("delete budget accessed");
        return "Delete Budget";
    }

}

// POST /api/budgets
// GET /api/budgets
// GET /api/budgets/{id}
// PUT /api/budgets/{id}
// DELETE /api/budgets/{id}

// POST /api/budgets/{id}/submit
// POST /api/budgets/{id}/approve
// POST /api/budgets/{id}/reject
