package org.sicredi.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class RandomUserResponse {
    private List<Result> results;

    @Data
    public static class Result {
        private Name name;
        private Location location;
        private String email;
        private String phone;
        private String cell;

        /**
         * Retorna o nome completo combinando 'first' e 'last'.
         */
        public String getFullName() {
            return name.getFirst() + " " + name.getLast();
        }

        /**
         * Retorna o endereço completo concatenando rua, cidade, estado e país.
         */
        public String getFullAddress() {
            return location.getStreet().getNumber() + " " + location.getStreet().getName() + ", "
                    + location.getCity() + " - " + location.getState() + ", "
                    + location.getCountry();
        }

        @Data
        public static class Name {
            private String title;
            private String first;
            private String last;
        }

        @Data
        public static class Location {
            private Street street;
            private String city;
            private String state;
            private String country;
            private String postcode;

            @Data
            public static class Street {
                private int number;
                private String name;
            }
        }
    }


}
