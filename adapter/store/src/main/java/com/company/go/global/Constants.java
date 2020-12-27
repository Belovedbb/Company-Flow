package com.company.go.global;

public class Constants {


    public enum Status{

        ACTIVE("ACTIVE"), INACTIVE("INACTIVE");

        private final String value;

        Status(final String value){
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public enum Access{
        LOCKED("LOCKED"), NOT_LOCKED("NOT_LOCKED");

        private final String value;

        Access(final String value){
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }


    public enum Roles{
        ROLE_ADMIN("ROLE_ADMIN");

        private final String value;

        Roles(final String value){
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

}


