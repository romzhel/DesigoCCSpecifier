package order_forms;

public enum CheckType {
    LETTERS_EN {
        boolean check(String value) {
            return value.matches(".*[а-яА-Я]+.*") || !value.matches(".*[a-zA-Z]+.*");
        }
    },
    LETTERS_RU {
        boolean check(String value) {
            return !value.matches(".*[а-яА-Я]+.*") || value.matches(".*[a-zA-Z]+.*");
        }
    },
    DIGITS {
        boolean check(String value) {
            return !value.matches("^\\d+$");
        }
    },
    MULTI_DIGITS {
        boolean check(String value) {
            return !value.matches("^[\\d\\,\\s]+$") || value.matches("^.*[\\,\\s]+$") ||
                    value.matches("^[\\,\\s]+.*$");
        }
    };

    abstract boolean check(String value);
}
