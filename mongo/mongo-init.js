db = db.getSiblingDB('task_manager_db');

db.createUser({
    user: "task_app_user",
    pwd: "pwd",
    roles: [
        {
            role: "readWrite",
            db: "task_manager_db"
        }
    ]
});

db.tasks.createIndex(
    {
        description: "text",
        "subtasks.name": "text"
    },
    {
        name: "TextIndex_Description_SubtasksName"
    }
);

const initialTasks = [
    {
        taskId: 1,
        name: "Implement REST API for User Management",
        description: "Create endpoints for user CRUD operations using Spring Boot.",
        creationDate: new Date(),
        deadline: new Date(new Date().setDate(new Date().getDate() + 7)),
        category: "Backend",
        subtasks: [
            {
                name: "Define User Entity",
                description: "Create User entity class with necessary fields and annotations."
            },
            {name: "Implement UserRepository", description: "Set up MongoDB repository interface for User entity."},
            {name: "Create UserService", description: "Implement service layer with methods for CRUD operations."},
            {name: "Develop UserController", description: "Define REST endpoints in UserController class."}
        ]
    },
    {
        taskId: 2,
        name: "Refactor Legacy Codebase",
        description: "Refactor old code for better readability and maintainability.",
        creationDate: new Date(),
        deadline: new Date(new Date().setDate(new Date().getDate() + 14)),
        category: "Maintenance",
        subtasks: [
            {name: "Update Variable Names", description: "Rename variables in legacy classes for readability."},
            {name: "Simplify Loops", description: "Replace complex for-loops with streams where applicable."},
            {name: "Remove Unused Imports", description: "Clean up unused imports across classes."},
            {name: "Update Documentation", description: "Add Javadoc comments for better code understanding."}
        ]
    },
    {
        taskId: 3,
        name: "Optimize Database Queries",
        description: "Analyze and optimize MongoDB queries for better performance.",
        creationDate: new Date(),
        deadline: new Date(new Date().setDate(new Date().getDate() + 5)),
        category: "Performance",
        subtasks: [
            {name: "Identify Slow Queries", description: "Analyze query performance using MongoDB profiler."},
            {name: "Add Indexes", description: "Create indexes for frequently queried fields."},
            {name: "Use Aggregations", description: "Replace multiple queries with single aggregation pipeline."}
        ]
    },
    {
        taskId: 4,
        name: "Implement Authentication and Authorization",
        description: "Add JWT-based security to the application using Spring Security.",
        creationDate: new Date(),
        deadline: new Date(new Date().setDate(new Date().getDate() + 10)),
        category: "Security",
        subtasks: [
            {name: "Configure JWT", description: "Set up JWT generation and validation in Spring Security."},
            {name: "Add Role-Based Access Control", description: "Define roles and secure endpoints accordingly."},
            {name: "Update Login and Signup Endpoints", description: "Modify existing endpoints to handle JWT tokens."}
        ]
    },
    {
        taskId: 5,
        name: "Write Unit Tests for Service Layer",
        description: "Add JUnit and Mockito tests for service layer methods.",
        creationDate: new Date(),
        deadline: new Date(new Date().setDate(new Date().getDate() + 3)),
        category: "Testing",
        subtasks: [
            {name: "Test UserService Methods", description: "Write unit tests for all CRUD methods in UserService."},
            {name: "Mock Repositories", description: "Use Mockito to mock repository dependencies."},
            {name: "Assert Expected Results", description: "Use assertions to verify correctness of service methods."}
        ]
    }
];


db.tasks.insertMany(initialTasks);

const maxTaskId = Math.max(...initialTasks.map(task => task.taskId));

db.database_sequences.insertOne({
    _id: "task_sequence",
    seq: maxTaskId
});