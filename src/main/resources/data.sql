-- Dev seed data. Password for seeded accounts: password
-- BCrypt hash of "password".

CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO roles (id, name, description, created_at, updated_at)
VALUES
    (gen_random_uuid(), 'ADMIN', 'Administrator role', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'INSTRUCTOR', 'Instructor role', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'LEARNER', 'Learner role', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;

INSERT INTO users (id, role_id, email, full_name, password_hash, status, created_at, updated_at)
SELECT gen_random_uuid(), id, 'admin@example.com', 'System Administrator',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM roles WHERE name = 'ADMIN'
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (id, role_id, email, full_name, password_hash, status, created_at, updated_at)
SELECT gen_random_uuid(), id, 'instructor@example.com', 'John Smith',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM roles WHERE name = 'INSTRUCTOR'
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (id, role_id, email, full_name, password_hash, status, created_at, updated_at)
SELECT gen_random_uuid(), id, 'learner@example.com', 'Nguyen Van A',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM roles WHERE name = 'LEARNER'
ON CONFLICT (email) DO NOTHING;

INSERT INTO instructors (
    id, user_id, bio, phone, headline, expertise, experience_years,
    website_url, linkedin_url, status, created_at, updated_at
)
VALUES (
    gen_random_uuid(),
    (SELECT id FROM users WHERE email = 'instructor@example.com'),
    'Backend engineer and technical instructor focused on production systems.',
    '+12025550123',
    'Senior Java Backend Engineer and Technical Instructor',
    'Java, Spring Boot, Microservices, PostgreSQL',
    8,
    'https://johnsmith.dev',
    'https://www.linkedin.com/in/johnsmith',
    'APPROVED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
)
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO learners (
    id, user_id, bio, phone, date_of_birth, occupation, learning_goal,
    created_at, updated_at
)
VALUES (
    gen_random_uuid(),
    (SELECT id FROM users WHERE email = 'learner@example.com'),
    'Backend developer who loves learning new technologies.',
    '0905123456',
    DATE '2003-05-20',
    'Software Engineer',
    'Become proficient in Spring Boot and Microservices',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
)
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO categories (id, parent_id, name, slug, description, created_at, updated_at)
VALUES (gen_random_uuid(), NULL, 'Programming', 'programming', 'Programming courses', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (slug) DO NOTHING;

INSERT INTO categories (id, parent_id, name, slug, description, created_at, updated_at)
SELECT gen_random_uuid(), id, 'Backend Development', 'backend-development', 'Backend engineering courses', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM categories
WHERE slug = 'programming'
    AND NOT EXISTS (SELECT 1 FROM categories WHERE slug = 'backend-development');

INSERT INTO categories (id, parent_id, name, slug, description, created_at, updated_at)
SELECT gen_random_uuid(), id, 'Java', 'java', 'Java and JVM ecosystem courses', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM categories
WHERE slug = 'backend-development'
    AND NOT EXISTS (SELECT 1 FROM categories WHERE slug = 'java');

INSERT INTO categories (id, parent_id, name, slug, description, created_at, updated_at)
SELECT gen_random_uuid(), id, 'DevOps', 'devops', 'DevOps and delivery courses', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM categories
WHERE slug = 'programming'
    AND NOT EXISTS (SELECT 1 FROM categories WHERE slug = 'devops');

INSERT INTO courses (
    id, instructor_id, title, slug, short_description, description, thumbnail_url,
    price, level, language, status, created_at, updated_at, published_at
)
VALUES (
    gen_random_uuid(),
    (SELECT instructor.id FROM instructors instructor
     JOIN users seed_user ON seed_user.id = instructor.user_id
     WHERE seed_user.email = 'instructor@example.com'),
    'Spring Boot Masterclass',
    'spring-boot-masterclass',
    'Build production-ready backend applications with Spring Boot.',
    'Learn Spring Boot, PostgreSQL, Redis, Kafka and Docker through practical projects.',
    'courses/thumbnails/spring-boot-masterclass.png',
    499000.00, 'INTERMEDIATE', 'Vietnamese', 'PUBLISHED',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
)
ON CONFLICT (slug) DO NOTHING;

INSERT INTO course_categories (course_id, category_id)
VALUES
    ((SELECT id FROM courses WHERE slug = 'spring-boot-masterclass'), (SELECT id FROM categories WHERE slug = 'java')),
    ((SELECT id FROM courses WHERE slug = 'spring-boot-masterclass'), (SELECT id FROM categories WHERE slug = 'devops'))
ON CONFLICT (course_id, category_id) DO NOTHING;

INSERT INTO course_requirements (id, course_id, content, sort_order, created_at, updated_at)
SELECT gen_random_uuid(), id, 'Basic Java knowledge', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM courses
WHERE slug = 'spring-boot-masterclass'
    AND NOT EXISTS (
    SELECT 1 FROM course_requirements requirement
        WHERE requirement.course_id = courses.id
      AND requirement.content = 'Basic Java knowledge'
    );

INSERT INTO course_requirements (id, course_id, content, sort_order, created_at, updated_at)
SELECT gen_random_uuid(), id, 'Familiarity with relational databases', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM courses
WHERE slug = 'spring-boot-masterclass'
    AND NOT EXISTS (
        SELECT 1 FROM course_requirements requirement
        WHERE requirement.course_id = courses.id
            AND requirement.content = 'Familiarity with relational databases'
    );

INSERT INTO course_learning_outcomes (id, course_id, content, sort_order, created_at, updated_at)
SELECT gen_random_uuid(), id, 'Build REST APIs with Spring Boot', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM courses
WHERE slug = 'spring-boot-masterclass'
    AND NOT EXISTS (
    SELECT 1 FROM course_learning_outcomes outcome
        WHERE outcome.course_id = courses.id
      AND outcome.content = 'Build REST APIs with Spring Boot'
    );

INSERT INTO course_learning_outcomes (id, course_id, content, sort_order, created_at, updated_at)
SELECT gen_random_uuid(), id, 'Design PostgreSQL databases for backend systems', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM courses
WHERE slug = 'spring-boot-masterclass'
    AND NOT EXISTS (
        SELECT 1 FROM course_learning_outcomes outcome
        WHERE outcome.course_id = courses.id
            AND outcome.content = 'Design PostgreSQL databases for backend systems'
    );