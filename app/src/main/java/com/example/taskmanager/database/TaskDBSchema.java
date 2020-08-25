package com.example.taskmanager.database;

public class TaskDBSchema {
    public static final String NAME = "TaskDB.db";
    public static final int VERSION = 1;


    public static final class TaskTable {
        public static final String NAME = "TaskTable";

        public static final class COLS {
            public static final String ID = "id";
            public static final String TASK_ID = "uuid";
            public static final String TASK_NAME = "name";
            public static final String TASK_DESCRIPTION = "description";
            public static final String TASK_STATE = "state";
            public static final String TASK_DATE = "date";
            public static final String TASK_INITIATOR = "initiator";
        }

    }
}
