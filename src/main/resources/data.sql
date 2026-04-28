-- Default Admin User (password: 1234567890, BCrypt encoded)
INSERT IGNORE INTO users (id, name, email, password, role, created_at, email_verified, failed_login_attempts, mfa_enabled, token_version) VALUES
(1, 'Admin', 'shannu1@gmail.com', '$2a$10$bIe0IuFTaAwsOmUJ2KFxvOIoLgaiF.zarxVmgmQpKN754jjYcM6Pi', 'ADMIN', NOW(), TRUE, 0, FALSE, 0);

-- ========== CAREER PATHS (8 paths) ==========
INSERT IGNORE INTO career_paths (id, name, description, icon, avg_salary, demand_level) VALUES
(1, 'Full Stack Developer', 'Build complete web applications from frontend to backend. Master React, Node.js, databases, and cloud deployment.', 'Code', '$95,000 - $150,000', 'Very High'),
(2, 'Data Analyst', 'Transform raw data into actionable insights. Learn SQL, Python, Tableau, and statistical analysis.', 'BarChart3', '$70,000 - $110,000', 'High'),
(3, 'AI/ML Engineer', 'Design intelligent systems using machine learning, deep learning, and natural language processing.', 'Brain', '$120,000 - $180,000', 'Very High'),
(4, 'DevOps Engineer', 'Automate infrastructure, CI/CD pipelines, and cloud deployments for scalable applications.', 'Cloud', '$100,000 - $160,000', 'High'),
(5, 'Cybersecurity Analyst', 'Protect organizations from cyber threats using ethical hacking, SIEM tools, and security frameworks.', 'Shield', '$90,000 - $140,000', 'Very High'),
(6, 'Mobile App Developer', 'Create cross-platform mobile apps using React Native, Flutter, and native iOS/Android development.', 'Smartphone', '$85,000 - $140,000', 'High'),
(7, 'Cloud Architect', 'Design and manage cloud infrastructure on AWS, Azure, and GCP for enterprise solutions.', 'Server', '$130,000 - $190,000', 'Very High'),
(8, 'UI/UX Designer', 'Design beautiful, user-centered digital experiences using Figma, prototyping, and design systems.', 'Palette', '$75,000 - $125,000', 'High');

-- ========== COURSES (56 courses) ==========
INSERT IGNORE INTO courses (id, title, description, career_path_id, difficulty, duration_hours, topics, trending) VALUES
-- Full Stack Developer Courses (1-10)
(1, 'React.js 19 Masterclass', 'Master React 19 with hooks, context, server components, and Next.js integration for modern web apps.', 1, 'Intermediate', 40, 'JSX, Hooks, Context API, React Router, Server Components, Next.js, Testing', true),
(2, 'Node.js & Express Backend', 'Build production-grade REST APIs with Node.js, Express, and MongoDB for scalable backends.', 1, 'Intermediate', 35, 'Node.js, Express, REST APIs, MongoDB, Authentication, Deployment', true),
(3, 'Spring Boot Fullstack', 'Enterprise Java backend development with Spring Boot 3, JPA, Security, and microservices.', 1, 'Advanced', 45, 'Spring Boot, JPA, Hibernate, Spring Security, JWT, MySQL', true),
(4, 'TypeScript Deep Dive', 'Type-safe JavaScript development for modern, maintainable web applications.', 1, 'Intermediate', 20, 'Types, Interfaces, Generics, Decorators, Module System', true),
(5, 'Next.js 15 Full Course', 'Server-side rendering, static generation, and API routes with the latest Next.js framework.', 1, 'Advanced', 38, 'SSR, SSG, API Routes, Middleware, App Router, Server Actions', true),
(6, 'MongoDB & Mongoose', 'NoSQL database design, aggregation pipelines, and ODM with Mongoose for Node.js.', 1, 'Beginner', 18, 'MongoDB, Mongoose, Aggregation, Indexing, Schema Design', false),
(7, 'PostgreSQL Advanced', 'Master relational databases with PostgreSQL including performance tuning and advanced queries.', 1, 'Intermediate', 22, 'SQL, Joins, CTEs, Window Functions, Indexing, Partitioning', false),
(8, 'GraphQL with Apollo', 'Build efficient APIs with GraphQL, Apollo Server, and Apollo Client for React.', 1, 'Intermediate', 20, 'GraphQL, Apollo Server, Apollo Client, Subscriptions, Caching', false),
(9, 'Redis & Caching Strategies', 'In-memory data structures, caching patterns, and session management with Redis.', 1, 'Intermediate', 12, 'Redis, Caching, Pub/Sub, Session Store, Rate Limiting', false),
(10, 'Microservices with Spring Cloud', 'Design distributed systems with Spring Cloud, Eureka, API Gateway, and resilience patterns.', 1, 'Advanced', 42, 'Spring Cloud, Eureka, API Gateway, Circuit Breaker, Config Server', false),

-- Data Analyst Courses (11-18)
(11, 'Python for Data Analysis', 'Data manipulation and analysis using Pandas, NumPy, and Matplotlib for real-world datasets.', 2, 'Beginner', 30, 'Python, Pandas, NumPy, Matplotlib, Data Cleaning, EDA', true),
(12, 'SQL Mastery', 'Advanced SQL querying, optimization, and database design for data professionals.', 2, 'Intermediate', 25, 'SQL Joins, Subqueries, Window Functions, Indexing, Normalization', true),
(13, 'Tableau & Power BI', 'Create stunning data visualizations and interactive dashboards for business intelligence.', 2, 'Beginner', 20, 'Tableau, Power BI, Dashboard Design, Storytelling with Data', true),
(14, 'Statistics for Data Science', 'Essential statistics and probability for data-driven decision making.', 2, 'Intermediate', 30, 'Probability, Hypothesis Testing, Regression, Bayesian Statistics', true),
(15, 'Excel for Business Analytics', 'Advanced Excel with pivot tables, VLOOKUP, macros, and Power Query for analysts.', 2, 'Beginner', 15, 'Pivot Tables, VLOOKUP, Macros, Power Query, Charts', false),
(16, 'R Programming for Analytics', 'Statistical computing and graphics with R for data analysis and visualization.', 2, 'Intermediate', 28, 'R, ggplot2, dplyr, tidyr, Statistical Modeling, Shiny', false),
(17, 'Data Warehousing & ETL', 'Design data warehouses and build ETL pipelines for enterprise analytics.', 2, 'Advanced', 30, 'Data Warehousing, ETL, Snowflake, dbt, Dimensional Modeling', false),
(18, 'Google Analytics Mastery', 'Web analytics, user tracking, and conversion optimization with Google Analytics 4.', 2, 'Beginner', 12, 'GA4, Event Tracking, Conversions, Audience Segments, Reports', false),

-- AI/ML Engineer Courses (19-27)
(19, 'Machine Learning A-Z', 'Complete machine learning with scikit-learn, from regression to ensemble methods.', 3, 'Advanced', 50, 'Linear Regression, Decision Trees, SVM, Random Forest, XGBoost, Cross-Validation', true),
(20, 'Deep Learning with PyTorch', 'Neural networks, CNNs, RNNs, and Transformers using PyTorch for production ML.', 3, 'Advanced', 45, 'Neural Networks, CNNs, RNNs, Transformers, GANs, Transfer Learning', true),
(21, 'Natural Language Processing', 'Text processing, sentiment analysis, and LLM fine-tuning for language understanding.', 3, 'Advanced', 35, 'Tokenization, Word Embeddings, BERT, GPT, Sentiment Analysis, NER', true),
(22, 'MLOps & Model Deployment', 'Deploy and monitor ML models in production environments at scale.', 3, 'Advanced', 25, 'MLflow, Docker, Kubernetes, Model Serving, A/B Testing, Monitoring', true),
(23, 'Computer Vision with OpenCV', 'Image processing, object detection, and visual recognition systems.', 3, 'Advanced', 35, 'OpenCV, Image Processing, Object Detection, YOLO, Image Segmentation', false),
(24, 'TensorFlow Developer Certificate', 'Prepare for the TensorFlow certification with hands-on deep learning projects.', 3, 'Advanced', 40, 'TensorFlow, Keras, CNNs, NLP, Time Series, TFLite', true),
(25, 'Reinforcement Learning', 'Train agents to make decisions using Q-learning, policy gradients, and PPO.', 3, 'Advanced', 30, 'Q-Learning, Policy Gradient, PPO, OpenAI Gym, Multi-Agent RL', false),
(26, 'Generative AI & LLMs', 'Build applications with Large Language Models, prompt engineering, and RAG.', 3, 'Intermediate', 22, 'LLMs, Prompt Engineering, RAG, LangChain, Fine-Tuning, Embeddings', true),
(27, 'Data Engineering with Apache Spark', 'Big data processing and analytics with Apache Spark and PySpark.', 3, 'Advanced', 35, 'Apache Spark, PySpark, DataFrames, Spark SQL, Streaming', false),

-- DevOps Engineer Courses (28-34)
(28, 'Docker & Kubernetes', 'Container orchestration from basics to production-grade clusters and deployments.', 4, 'Intermediate', 35, 'Docker, Dockerfile, Kubernetes, Helm, Service Mesh, Monitoring', true),
(29, 'AWS Cloud Practitioner', 'Amazon Web Services fundamentals and cloud architecture for beginners.', 4, 'Beginner', 25, 'EC2, S3, RDS, Lambda, VPC, IAM, CloudFormation', true),
(30, 'CI/CD with GitHub Actions', 'Automate testing, building, and deploying with modern CI/CD pipelines.', 4, 'Intermediate', 15, 'GitHub Actions, YAML, Testing Automation, Docker Build, Deploy to AWS', true),
(31, 'Terraform Infrastructure as Code', 'Provision and manage cloud resources declaratively with HashiCorp Terraform.', 4, 'Intermediate', 25, 'Terraform, HCL, AWS, Azure, State Management, Modules', true),
(32, 'Linux System Administration', 'Master Linux shell, process management, networking, and server configuration.', 4, 'Beginner', 30, 'Bash, Shell Scripting, Networking, systemd, Package Management', false),
(33, 'Ansible Automation', 'Automate server configuration and application deployment with Ansible playbooks.', 4, 'Intermediate', 18, 'Ansible, Playbooks, Roles, Inventory, Vault, Tower', false),
(34, 'Prometheus & Grafana Monitoring', 'Set up production monitoring, alerting, and dashboards for distributed systems.', 4, 'Intermediate', 16, 'Prometheus, Grafana, Alertmanager, PromQL, Dashboards', false),

-- Cybersecurity Courses (35-41)
(35, 'Ethical Hacking Fundamentals', 'Learn penetration testing, vulnerability assessment, and security tools.', 5, 'Intermediate', 40, 'Kali Linux, Nmap, Metasploit, Burp Suite, OWASP Top 10', true),
(36, 'Network Security', 'Protect networks using firewalls, IDS/IPS, and security protocols.', 5, 'Intermediate', 30, 'Firewalls, VPN, IDS/IPS, SSL/TLS, Network Monitoring', true),
(37, 'Web Application Security', 'Secure web applications against SQL injection, XSS, CSRF, and more.', 5, 'Intermediate', 25, 'SQL Injection, XSS, CSRF, Authentication Security, OWASP', true),
(38, 'CompTIA Security+ Prep', 'Comprehensive preparation for the CompTIA Security+ certification exam.', 5, 'Beginner', 35, 'Risk Management, Cryptography, Identity Management, Compliance', true),
(39, 'Digital Forensics', 'Investigate cybercrime using forensic tools and evidence collection techniques.', 5, 'Advanced', 30, 'Disk Forensics, Memory Analysis, Network Forensics, Autopsy, FTK', false),
(40, 'Cloud Security', 'Secure cloud environments on AWS, Azure, and GCP with best practices.', 5, 'Advanced', 25, 'Cloud IAM, Encryption, VPC Security, Compliance, CSPM', false),
(41, 'Malware Analysis', 'Reverse engineer malware and understand attack techniques for defense.', 5, 'Advanced', 28, 'Static Analysis, Dynamic Analysis, Reverse Engineering, Sandboxing', false),

-- Mobile App Developer Courses (42-47)
(42, 'React Native Development', 'Build cross-platform mobile apps with React Native and Expo framework.', 6, 'Intermediate', 35, 'React Native, Expo, Navigation, State Management, Native Modules', true),
(43, 'Flutter & Dart Complete', 'Google UI toolkit for building natively compiled mobile applications.', 6, 'Intermediate', 35, 'Dart, Flutter Widgets, State Management, Firebase, Animations', true),
(44, 'iOS Development with SwiftUI', 'Build native iOS apps using SwiftUI and the Apple development ecosystem.', 6, 'Intermediate', 40, 'SwiftUI, Combine, Core Data, CloudKit, App Store Deployment', true),
(45, 'Android Development with Kotlin', 'Native Android development using Kotlin, Jetpack Compose, and modern architecture.', 6, 'Intermediate', 40, 'Kotlin, Jetpack Compose, MVVM, Room, Retrofit, Coroutines', true),
(46, 'Mobile App UI/UX Design', 'Design mobile-first user experiences with Material Design and Human Interface Guidelines.', 6, 'Beginner', 18, 'Material Design, HIG, Prototyping, User Testing, Accessibility', false),
(47, 'Firebase for Mobile Apps', 'Backend-as-a-service for mobile apps with authentication, database, and analytics.', 6, 'Beginner', 15, 'Firebase Auth, Firestore, Cloud Functions, Analytics, Crashlytics', false),

-- Cloud Architect Courses (48-52)
(48, 'AWS Solutions Architect', 'Design highly available, cost-efficient, and scalable systems on AWS.', 7, 'Advanced', 50, 'VPC, EC2, S3, RDS, DynamoDB, Lambda, CloudFront, Route53', true),
(49, 'Azure Cloud Engineer', 'Microsoft Azure services, architecture, and certifications for cloud professionals.', 7, 'Intermediate', 40, 'Azure VMs, Blob Storage, Azure AD, Functions, AKS', true),
(50, 'Google Cloud Platform', 'GCP services and architecture for building scalable cloud applications.', 7, 'Intermediate', 35, 'Compute Engine, Cloud Storage, BigQuery, Cloud Run, Pub/Sub', true),
(51, 'Serverless Architecture', 'Design event-driven serverless applications with AWS Lambda, API Gateway, and DynamoDB.', 7, 'Advanced', 22, 'Lambda, API Gateway, Step Functions, EventBridge, SQS', false),
(52, 'Multi-Cloud Strategy', 'Design resilient multi-cloud architectures spanning AWS, Azure, and GCP.', 7, 'Advanced', 28, 'Multi-Cloud, Cloud Migration, Hybrid Cloud, Cost Optimization', false),

-- UI/UX Designer Courses (53-56)
(53, 'Figma UI Design Masterclass', 'Master Figma for creating modern, responsive user interfaces and design systems.', 8, 'Beginner', 20, 'Figma, Auto Layout, Components, Prototyping, Design Systems', true),
(54, 'UX Research & Strategy', 'User research methodologies, personas, journey maps, and UX strategy.', 8, 'Intermediate', 25, 'User Interviews, Personas, Journey Maps, Usability Testing, A/B Testing', true),
(55, 'Design Systems Engineering', 'Build and maintain scalable design systems for product teams.', 8, 'Advanced', 22, 'Design Tokens, Component Libraries, Documentation, Storybook', false),
(56, 'Motion Design for UI', 'Create engaging micro-interactions and animations for web and mobile interfaces.', 8, 'Intermediate', 18, 'Framer Motion, CSS Animations, Lottie, Interaction Design, Easing', true);

-- ========== QUIZZES (10 quizzes) ==========
INSERT IGNORE INTO quizzes (id, title, company_name, career_path_id, difficulty) VALUES
(1, 'Google Frontend Interview', 'Google', 1, 'Hard'),
(2, 'Amazon SDE Interview', 'Amazon', 1, 'Hard'),
(3, 'Meta React Interview', 'Meta', 1, 'Medium'),
(4, 'Google Data Analyst', 'Google', 2, 'Medium'),
(5, 'Microsoft ML Engineer', 'Microsoft', 3, 'Hard'),
(6, 'AWS DevOps Interview', 'Amazon', 4, 'Medium'),
(7, 'CrowdStrike Security', 'CrowdStrike', 5, 'Hard'),
(8, 'Apple iOS Interview', 'Apple', 6, 'Hard'),
(9, 'Netflix System Design', 'Netflix', 7, 'Hard'),
(10, 'Spotify UX Interview', 'Spotify', 8, 'Medium');

-- ========== QUIZ QUESTIONS (30+ questions) ==========
INSERT IGNORE INTO quiz_questions (id, quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option, explanation) VALUES
-- Google Frontend Interview
(1, 1, 'What is the virtual DOM in React?', 'A database for storing UI state', 'A lightweight copy of the real DOM for efficient updates', 'A browser extension for debugging', 'A CSS rendering engine', 'B', 'The virtual DOM is an in-memory representation of the real DOM. React uses it to compute the minimal set of changes needed, making updates efficient.'),
(2, 1, 'Which hook replaces component lifecycle methods?', 'useState', 'useEffect', 'useRef', 'useMemo', 'B', 'useEffect combines componentDidMount, componentDidUpdate, and componentWillUnmount into a single API.'),
(3, 1, 'What is the time complexity of JavaScript Array.sort()?', 'O(n)', 'O(n log n)', 'O(n^2)', 'O(log n)', 'B', 'Most JavaScript engines use TimSort, which has O(n log n) average and worst-case complexity.'),
(4, 1, 'What does the CSS property will-change do?', 'Triggers an animation', 'Hints the browser to optimize for upcoming changes', 'Changes the z-index', 'Forces a repaint', 'B', 'will-change tells the browser to create a new compositing layer, enabling GPU-accelerated rendering.'),

-- Amazon SDE Interview
(5, 2, 'What is the time complexity of HashMap get() in Java?', 'O(1) amortized', 'O(n)', 'O(log n)', 'O(n^2)', 'A', 'HashMap uses hashing for O(1) average-case lookup. Worst case is O(n) with hash collisions.'),
(6, 2, 'Which design pattern does Spring Boot Dependency Injection use?', 'Singleton', 'Factory', 'Inversion of Control', 'Observer', 'C', 'Spring uses IoC (Inversion of Control) where the framework manages object creation and lifecycle.'),
(7, 2, 'What is the CAP theorem?', 'A performance metric', 'A theorem about distributed system trade-offs', 'A database normalization rule', 'A caching strategy', 'B', 'CAP states that a distributed system can only guarantee two of three: Consistency, Availability, Partition tolerance.'),

-- Meta React Interview
(8, 3, 'What is React Fiber?', 'A new rendering library', 'React internal reconciliation engine', 'A state management tool', 'A testing framework', 'B', 'React Fiber is the reimplemented core reconciliation algorithm that enables incremental rendering and prioritized updates.'),
(9, 3, 'What pattern does useReducer follow?', 'Observer pattern', 'Flux/Redux pattern', 'MVC pattern', 'Singleton pattern', 'B', 'useReducer follows the Flux pattern with actions dispatched to a reducer function to compute new state.'),
(10, 3, 'What is code splitting in React?', 'Separating CSS from JS', 'Loading components on demand with lazy()', 'Using multiple entry points', 'Breaking code into microservices', 'B', 'Code splitting with React.lazy() and Suspense allows loading components only when needed, reducing initial bundle size.'),

-- Google Data Analyst
(11, 4, 'What is a window function in SQL?', 'A function that opens a new database connection', 'A function that performs calculations across a set of rows', 'A function for creating views', 'A function for managing transactions', 'B', 'Window functions perform calculations across a set of table rows related to the current row, without collapsing results like GROUP BY.'),
(12, 4, 'What is the difference between INNER JOIN and LEFT JOIN?', 'No difference', 'LEFT JOIN includes unmatched rows from the left table', 'INNER JOIN is faster', 'LEFT JOIN only works with two tables', 'B', 'LEFT JOIN returns all rows from the left table plus matched rows from the right table, with NULLs for non-matches.'),

-- Microsoft ML Engineer
(13, 5, 'What is gradient descent?', 'A data visualization technique', 'An optimization algorithm to minimize loss', 'A feature engineering method', 'A model evaluation metric', 'B', 'Gradient descent iteratively adjusts model parameters in the direction that minimizes the loss function.'),
(14, 5, 'What is overfitting?', 'Model performs well on all data', 'Model memorizes training data but fails on new data', 'Model is too simple', 'Model has high bias', 'B', 'Overfitting occurs when a model learns noise in training data, resulting in poor generalization to unseen data.'),

-- AWS DevOps
(15, 6, 'What is Infrastructure as Code?', 'Writing code on servers', 'Managing infrastructure through machine-readable files', 'Using coding languages for DevOps', 'A cloud computing service', 'B', 'IaC manages and provisions infrastructure through code rather than manual processes.'),
(16, 6, 'What is a Docker container?', 'A virtual machine', 'A lightweight, isolated process with its own filesystem', 'A cloud service', 'A database type', 'B', 'Containers package an application with its dependencies, sharing the host OS kernel for lightweight isolation.'),

-- CrowdStrike Security
(17, 7, 'What is the OWASP Top 10?', 'A list of programming languages', 'Top 10 critical web application security risks', 'A firewall configuration', 'A penetration testing tool', 'B', 'OWASP Top 10 is a standard awareness document listing the most critical security risks to web applications.'),
(18, 7, 'What is SQL injection?', 'A database backup method', 'Inserting malicious SQL through user input', 'A SQL optimization technique', 'A database migration strategy', 'B', 'SQL injection exploits vulnerable applications by inserting malicious SQL code through unvalidated user input fields.'),

-- Apple iOS Interview
(19, 8, 'What is SwiftUI?', 'A testing framework', 'A declarative UI framework for Apple platforms', 'A database library', 'A networking library', 'B', 'SwiftUI is Apple modern declarative framework for building user interfaces across all Apple platforms.'),
(20, 8, 'What is the difference between struct and class in Swift?', 'No difference', 'Structs are value types, classes are reference types', 'Classes are faster', 'Structs support inheritance', 'B', 'In Swift, structs are value types (copied on assignment) while classes are reference types (shared references).'),

-- Netflix System Design
(21, 9, 'What is a CDN?', 'A coding standard', 'A distributed network for delivering content closer to users', 'A database clustering method', 'A CI/CD tool', 'B', 'A Content Delivery Network caches content at edge locations worldwide to reduce latency for end users.'),
(22, 9, 'What is horizontal scaling?', 'Adding more RAM to a server', 'Adding more servers to distribute load', 'Increasing CPU cores', 'Using faster storage', 'B', 'Horizontal scaling (scaling out) means adding more machines to a pool, distributing workload across multiple servers.'),
(23, 9, 'What is eventual consistency?', 'Always consistent data', 'Data becomes consistent over time after updates', 'Inconsistent database state', 'A locking mechanism', 'B', 'Eventual consistency guarantees that, given enough time, all replicas will converge to the same state after an update.'),

-- Spotify UX Interview
(24, 10, 'What is a design system?', 'A CSS framework', 'A collection of reusable components and design standards', 'A wireframing tool', 'A project management methodology', 'B', 'A design system is a set of reusable components, guidelines, and design tokens that ensure consistency across products.'),
(25, 10, 'What is the difference between UX and UI design?', 'They are the same', 'UX focuses on user experience, UI on visual interface', 'UI is more important', 'UX only involves wireframes', 'B', 'UX design focuses on the overall user experience and usability, while UI design focuses on the visual elements and interactions.');

-- ========== ASSESSMENT QUESTIONS (20 questions) ==========
INSERT IGNORE INTO questions (id, category, text) VALUES
(1, 'ANALYTICAL', 'I enjoy solving complex problems logically.'),
(2, 'ANALYTICAL', 'I am comfortable working with numbers and analyzing data.'),
(3, 'ANALYTICAL', 'I prefer structured workflows over spontaneous tasks.'),
(4, 'ANALYTICAL', 'I can easily identify patterns in complex systems.'),
(5, 'ANALYTICAL', 'I like to uncover the root cause behind an issue.'),
(6, 'CREATIVE', 'I frequently come up with original ideas.'),
(7, 'CREATIVE', 'I enjoy designing visuals, writing, or composing.'),
(8, 'CREATIVE', 'I prefer open-ended tasks where I set the direction.'),
(9, 'CREATIVE', 'I value aesthetics and design in the products I use.'),
(10, 'CREATIVE', 'I find unconventional ways to approach conventional problems.'),
(11, 'TECHNICAL', 'I enjoy learning how machines or software work internally.'),
(12, 'TECHNICAL', 'I am interested in coding, scripting, or building circuits.'),
(13, 'TECHNICAL', 'I can easily understand and apply technical documentation.'),
(14, 'TECHNICAL', 'I quickly adopt and master new software tools.'),
(15, 'TECHNICAL', 'I enjoy troubleshooting technical devices or programs.'),
(16, 'SOCIAL', 'I naturally take on leadership or coordinating roles in groups.'),
(17, 'SOCIAL', 'I find it easy to empathize and connect with new people.'),
(18, 'SOCIAL', 'I thrive in environments requiring heavy collaboration.'),
(19, 'SOCIAL', 'I am skilled at resolving conflicts between team members.'),
(20, 'SOCIAL', 'I enjoy presenting information to a large audience.');

-- ========== ADDITIONAL CODING PROBLEMS (3-10) ==========
INSERT IGNORE INTO problems (id, title, description, difficulty, tags, base_code, xp_reward) VALUES
(3, 'Reverse a String', 'Write a function that reverses a given string.\n\nExample:\nInput: "hello"\nOutput: "olleh"\n\nConstraints:\n- 1 <= s.length <= 10^5\n- s consists of printable ASCII characters', 'EASY', 'Strings, Two Pointers', 'class Solution {\n    public String reverse(String s) {\n        // Your implementation\n        return "";\n    }\n}', 75),
(4, 'Palindrome Check', 'Given a string, determine if it is a palindrome, considering only alphanumeric characters and ignoring cases.\n\nExample 1:\nInput: "A man, a plan, a canal: Panama"\nOutput: true\n\nExample 2:\nInput: "race a car"\nOutput: false\n\nConstraints:\n- 1 <= s.length <= 2 * 10^5', 'EASY', 'Strings, Two Pointers', 'class Solution {\n    public boolean isPalindrome(String s) {\n        // Your implementation\n        return false;\n    }\n}', 75),
(5, 'FizzBuzz', 'Given an integer n, return a string array answer where:\n- answer[i] == "FizzBuzz" if i is divisible by 3 and 5\n- answer[i] == "Fizz" if i is divisible by 3\n- answer[i] == "Buzz" if i is divisible by 5\n- answer[i] == i (as a string) otherwise\n\nExample:\nInput: n = 15\nOutput: ["1","2","Fizz","4","Buzz","Fizz","7","8","Fizz","Buzz","11","Fizz","13","14","FizzBuzz"]', 'EASY', 'Math, Simulation', 'class Solution {\n    public List<String> fizzBuzz(int n) {\n        // Your implementation\n        return new ArrayList<>();\n    }\n}', 50),
(6, 'Maximum Subarray', 'Given an integer array nums, find the subarray with the largest sum, and return its sum.\n\nExample:\nInput: nums = [-2,1,-3,4,-1,2,1,-5,4]\nOutput: 6\nExplanation: The subarray [4,-1,2,1] has the largest sum 6.\n\nConstraints:\n- 1 <= nums.length <= 10^5\n- -10^4 <= nums[i] <= 10^4', 'MEDIUM', 'Arrays, Dynamic Programming, Kadane', 'class Solution {\n    public int maxSubArray(int[] nums) {\n        // Your implementation\n        return 0;\n    }\n}', 200),
(7, 'Valid Parentheses', 'Given a string s containing just the characters ''('', '')'', ''{'', ''}'', ''['' and '']'', determine if the input string is valid.\n\nA string is valid if:\n- Open brackets are closed by the same type.\n- Open brackets are closed in the correct order.\n\nExample:\nInput: s = "()[]{}"\nOutput: true\n\nInput: s = "(]"\nOutput: false', 'EASY', 'Stack, Strings', 'class Solution {\n    public boolean isValid(String s) {\n        // Your implementation\n        return false;\n    }\n}', 100),
(8, 'Merge Sorted Arrays', 'You are given two integer arrays nums1 and nums2, sorted in non-decreasing order. Merge nums2 into nums1 as one sorted array.\n\nExample:\nInput: nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3\nOutput: [1,2,2,3,5,6]\n\nConstraints:\n- nums1.length == m + n\n- nums2.length == n', 'MEDIUM', 'Arrays, Two Pointers, Sorting', 'class Solution {\n    public void merge(int[] nums1, int m, int[] nums2, int n) {\n        // Your implementation\n    }\n}', 200),
(9, 'Longest Common Subsequence', 'Given two strings text1 and text2, return the length of their longest common subsequence. If there is no common subsequence, return 0.\n\nExample:\nInput: text1 = "abcde", text2 = "ace"\nOutput: 3\nExplanation: The longest common subsequence is "ace" and its length is 3.\n\nConstraints:\n- 1 <= text1.length, text2.length <= 1000', 'HARD', 'Dynamic Programming, Strings', 'class Solution {\n    public int longestCommonSubsequence(String text1, String text2) {\n        // Your implementation\n        return 0;\n    }\n}', 400),
(10, 'Binary Search', 'Given a sorted array of distinct integers and a target value, return the index if the target is found. If not, return -1.\n\nYou must write an algorithm with O(log n) runtime complexity.\n\nExample:\nInput: nums = [-1,0,3,5,9,12], target = 9\nOutput: 4', 'EASY', 'Arrays, Binary Search', 'class Solution {\n    public int search(int[] nums, int target) {\n        // Your implementation\n        return -1;\n    }\n}', 100);

-- ========== TEST CASES FOR ALL PROBLEMS ==========
INSERT IGNORE INTO test_cases (id, problem_id, input, expected_output, is_hidden) VALUES
-- Two Sum (problem 1)
(1, 1, 'nums = [2,7,11,15], target = 9', '[0,1]', false),
(2, 1, 'nums = [3,2,4], target = 6', '[1,2]', false),
(3, 1, 'nums = [3,3], target = 6', '[0,1]', true),
-- Unique Paths (problem 2)
(4, 2, 'm = 3, n = 7', '28', false),
(5, 2, 'm = 3, n = 2', '3', false),
(6, 2, 'm = 1, n = 1', '1', true),
-- Reverse String (problem 3)
(7, 3, 's = "hello"', '"olleh"', false),
(8, 3, 's = "Hannah"', '"hannaH"', false),
(9, 3, 's = "a"', '"a"', true),
-- Palindrome Check (problem 4)
(10, 4, 's = "A man, a plan, a canal: Panama"', 'true', false),
(11, 4, 's = "race a car"', 'false', false),
(12, 4, 's = " "', 'true', true),
-- FizzBuzz (problem 5)
(13, 5, 'n = 5', '["1","2","Fizz","4","Buzz"]', false),
(14, 5, 'n = 15', '["1","2","Fizz","4","Buzz","Fizz","7","8","Fizz","Buzz","11","Fizz","13","14","FizzBuzz"]', false),
-- Maximum Subarray (problem 6)
(15, 6, 'nums = [-2,1,-3,4,-1,2,1,-5,4]', '6', false),
(16, 6, 'nums = [1]', '1', false),
(17, 6, 'nums = [5,4,-1,7,8]', '23', true),
-- Valid Parentheses (problem 7)
(18, 7, 's = "()"', 'true', false),
(19, 7, 's = "()[]{}"', 'true', false),
(20, 7, 's = "(]"', 'false', false),
(21, 7, 's = "([)]"', 'false', true),
-- Merge Sorted Arrays (problem 8)
(22, 8, 'nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3', '[1,2,2,3,5,6]', false),
(23, 8, 'nums1 = [1], m = 1, nums2 = [], n = 0', '[1]', true),
-- Longest Common Subsequence (problem 9)
(24, 9, 'text1 = "abcde", text2 = "ace"', '3', false),
(25, 9, 'text1 = "abc", text2 = "abc"', '3', false),
(26, 9, 'text1 = "abc", text2 = "def"', '0', true),
-- Binary Search (problem 10)
(27, 10, 'nums = [-1,0,3,5,9,12], target = 9', '4', false),
(28, 10, 'nums = [-1,0,3,5,9,12], target = 2', '-1', false),
(29, 10, 'nums = [5], target = 5', '0', true);
