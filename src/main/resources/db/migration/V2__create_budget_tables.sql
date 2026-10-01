CREATE TABLE financial_year (
    id BIGSERIAL PRIMARY KEY,

    year_code VARCHAR(20) NOT NULL UNIQUE,

    start_date DATE NOT NULL,

    end_date DATE NOT NULL,

    status VARCHAR(20) NOT NULL DEFAULT 'OPEN',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_financial_year_dates
        CHECK (start_date < end_date),

    CONSTRAINT chk_financial_year_status
        CHECK (status IN ('OPEN', 'CLOSED'))
);


CREATE TABLE budget (
    id BIGSERIAL PRIMARY KEY,

    financial_year_id BIGINT NOT NULL,

    budget_code VARCHAR(50) NOT NULL UNIQUE,

    budget_name VARCHAR(255) NOT NULL,

    description TEXT,

    total_amount NUMERIC(18,2) NOT NULL DEFAULT 0,

    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',

    created_by BIGINT,

    approved_by BIGINT,

    approved_at TIMESTAMP,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_budget_financial_year
        FOREIGN KEY (financial_year_id)
        REFERENCES financial_year(id),

    CONSTRAINT chk_budget_amount
        CHECK (total_amount >= 0),

    CONSTRAINT chk_budget_status
        CHECK (
            status IN (
                'DRAFT',
                'SUBMITTED',
                'UNDER_REVIEW',
                'APPROVED',
                'REJECTED',
                'CLOSED'
            )
        )
);


CREATE TABLE budget_head (
    id BIGSERIAL PRIMARY KEY,

    budget_id BIGINT NOT NULL,

    head_code VARCHAR(100) NOT NULL,

    head_name VARCHAR(255) NOT NULL,

    description TEXT,

    allocated_amount NUMERIC(18,2) NOT NULL DEFAULT 0,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_budget_head_budget
        FOREIGN KEY (budget_id)
        REFERENCES budget(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_budget_head_amount
        CHECK (allocated_amount >= 0),

    CONSTRAINT uq_budget_head_code
        UNIQUE (budget_id, head_code)
);


CREATE TABLE budget_allocation (
    id BIGSERIAL PRIMARY KEY,

    budget_id BIGINT NOT NULL,

    budget_head_id BIGINT NOT NULL,

    allocation_code VARCHAR(100) NOT NULL UNIQUE,

    allocated_amount NUMERIC(18,2) NOT NULL,

    allocated_to_department VARCHAR(255),

    allocated_to_division VARCHAR(255),

    allocated_to_range VARCHAR(255),

    allocation_date DATE NOT NULL,

    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',

    created_by BIGINT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_allocation_budget
        FOREIGN KEY (budget_id)
        REFERENCES budget(id),

    CONSTRAINT fk_allocation_budget_head
        FOREIGN KEY (budget_head_id)
        REFERENCES budget_head(id),

    CONSTRAINT chk_allocation_amount
        CHECK (allocated_amount >= 0),

    CONSTRAINT chk_allocation_status
        CHECK (
            status IN (
                'ACTIVE',
                'CANCELLED',
                'CLOSED'
            )
        )
);


CREATE INDEX idx_budget_financial_year
ON budget(financial_year_id);

CREATE INDEX idx_budget_status
ON budget(status);

CREATE INDEX idx_budget_head_budget
ON budget_head(budget_id);

CREATE INDEX idx_budget_allocation_budget
ON budget_allocation(budget_id);

CREATE INDEX idx_budget_allocation_head
ON budget_allocation(budget_head_id);

CREATE INDEX idx_budget_allocation_department
ON budget_allocation(allocated_to_department);
