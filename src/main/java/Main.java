import java.util.*;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        PrintNumbers print = new PrintNumbers();
        print.printNumbers();
    }
}

class PrintNumbers {
    Instructions Instructions = new Instructions();
    Input Input = new Input();
    PropertiesCheck PropertiesCheck = new PropertiesCheck();
    NumbersCheck NumbersCheck = new NumbersCheck();
    Error Error = new Error();

    Long number;
    Long size;
    String[] properties;

    void printNumbers() {
        Instructions.printInstructions();

        while (true) {
            try {
                String[] input = Input.getInput();
                System.out.println();

                if (input[0].isBlank()) {
                    Instructions.printInstructions();
                }
                else if (input[0].equals("0")) {
                    System.out.println("Goodbye!");
                    return;
                }
                else if (input.length == 1) {
                    number = Long.valueOf(input[0]);

                    if (Error.naturalCheck(input)) {
                        NumbersCheck.singleNumber(number);
                    }
                }
                else if (input.length == 2) {
                    number = Long.valueOf(input[0]);
                    size = Long.valueOf(input[1]);

                    if (Error.naturalCheck(input)) {
                        NumbersCheck.multiNumbers(number, size);
                    }
                }
                else {
                    number = Long.valueOf(input[0]);
                    size = Long.valueOf(input[1]);
                    properties = Input.getProperties(input);

                    if (PropertiesCheck.checkProperties(properties)) {
                        NumbersCheck.numbersAndProperties(number, size, properties);
                    }
                }
            }
            catch (NumberFormatException e) {
                System.out.println("The first parameter should be a natural number or zero.\n");
            }
        }
    }
}

class Input {

    String[] getInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a request: ");
        return scanner.nextLine().split(" ");
    }

    String[] getProperties(String[] p) {
        int size = p.length - 2;
        String[] properties = new String[size];
        for (int i = 0; i < properties.length; i++) {
            properties[i] = p[2 + i].toUpperCase();
        }
        return properties;
    }
}

class Properties {
    BooleanCheck check = new BooleanCheck();

    Map<String, Boolean> includePropertiesMap(Long n) {
        Map<String, Boolean> includeProperties = new TreeMap<>();
        includeProperties.put("EVEN", check.even(n));
        includeProperties.put("ODD", check.odd(n));
        includeProperties.put("BUZZ", check.buzz(n));
        includeProperties.put("DUCK", check.duck(n));
        includeProperties.put("PALINDROMIC", check.palindromic(n));
        includeProperties.put("GAPFUL", check.gapful(n));
        includeProperties.put("SPY", check.spy(n));
        includeProperties.put("SQUARE", check.square(n));
        includeProperties.put("SUNNY", check.sunny(n));
        includeProperties.put("JUMPING", check.jumping(n));
        includeProperties.put("HAPPY", check.happy(n));
        includeProperties.put("SAD", check.sad(n));
        return includeProperties;
    }

    Map<String, Boolean> excludePropertiesMap(Long n) {
        Map<String, Boolean> excludeProperties = new TreeMap<>();
        excludeProperties.put("-EVEN", check.even(n));
        excludeProperties.put("-ODD", check.odd(n));
        excludeProperties.put("-BUZZ", check.buzz(n));
        excludeProperties.put("-DUCK", check.duck(n));
        excludeProperties.put("-PALINDROMIC", check.palindromic(n));
        excludeProperties.put("-GAPFUL", check.gapful(n));
        excludeProperties.put("-SPY", check.spy(n));
        excludeProperties.put("-SQUARE", check.square(n));
        excludeProperties.put("-SUNNY", check.sunny(n));
        excludeProperties.put("-JUMPING", check.jumping(n));
        excludeProperties.put("-HAPPY", check.happy(n));
        excludeProperties.put("-SAD", check.sad(n));
        return excludeProperties;
    }

    Map<String, Boolean> includeAndExcludePropertiesMap(Long n) {
        Map<String, Boolean> includeAndExcludePropertiesMap = new TreeMap<>();
        includeAndExcludePropertiesMap.putAll(includePropertiesMap(n));
        includeAndExcludePropertiesMap.putAll(excludePropertiesMap(n));
        return includeAndExcludePropertiesMap;
    }

    Set<String> availableProperties() {
        return new TreeSet<>(includePropertiesMap(1L).keySet());
    }

    Set<String> allPossibleProperties() {
        return new TreeSet<>(includeAndExcludePropertiesMap(1L).keySet());
    }

}

class PropertiesCheck {

    Properties Properties = new Properties();
    Error Error = new Error();

    Boolean inputContainsAvailableProperties(String[] p) {
        Set<String> allPossibleProperties = Properties.allPossibleProperties();
        boolean result = true;
        for (String properties : p) {
            result = result && allPossibleProperties.contains(properties);
        }
        return result;
    }

    void checkWrongProperties(String[] p) {
        Set<String> availableProperties = Properties.availableProperties();
        Set<String> allPossibleProperties = Properties.allPossibleProperties();
        Set<String> wrongProperties = new TreeSet<>();

        for (String properties : p) {
            if (!allPossibleProperties.contains(properties)) {
                wrongProperties.add(properties);
            }
        }
        if (wrongProperties.size() == 1) {
            System.out.printf("The property %s is wrong. Available properties: %s\n\n", wrongProperties, availableProperties);
        }
        else {
            System.out.printf("The properties %s are wrong. Available properties: %s\n\n", wrongProperties, availableProperties);
        }
    }

    Boolean checkProperties(String[] p) {
        boolean result = false;
        if (inputContainsAvailableProperties(p)) {
            if (!Error.mutually(p)) {
                result = true;
            }
        }
        else {
            checkWrongProperties(p);
        }
        return result;
    }

    Set<String> propertiesInclude(String[] p) {
        Set<String> propertiesToCheck = new TreeSet<>();
        for (String properties : p) {
            if (Properties.includePropertiesMap(1L).containsKey(properties)) {
                propertiesToCheck.add(properties);
            }
        }
        return propertiesToCheck;
    }

    Set<String> propertiesExclude(String[] p) {
        Set<String> propertiesToCheck = new TreeSet<>();
        for (String properties : p) {
            if (Properties.excludePropertiesMap(1L).containsKey(properties)) {
                propertiesToCheck.add(properties);
            }
        }
        return propertiesToCheck;
    }

    Boolean checkAllProperties(Map<String, Boolean> map, String[] p) {
        boolean result = true;

        Set<String> propertiesInclude = propertiesInclude(p);
        Set<String> propertiesExclude = propertiesExclude(p);

        if (!propertiesInclude.isEmpty() && !propertiesExclude.isEmpty()) {
            for (String include : propertiesInclude) {
                for (String exclude : propertiesExclude) {
                    result = result && map.get(include) && !map.get(exclude);
                }
            }
        }
        else if (!propertiesInclude.isEmpty()) {
            for (String include : propertiesInclude) {
                result = result && map.get(include);
            }
        }
        else {
            for (String exclude : propertiesExclude) {
                result = result && !map.get(exclude);
            }
        }
        return result;
    }
}

class NumbersCheck {
    Properties Properties = new Properties();
    PropertiesCheck PropertiesCheck = new PropertiesCheck();

    void singleNumber(Long n) {
        Map<String, Boolean> propertiesMap = Properties.includePropertiesMap(n);
        System.out.printf("Properties of %d\n", n);
        Set<String> propertiesSet = propertiesMap.keySet();
        for (String properties : propertiesSet) {
            System.out.printf("%15s: %s\n", properties.toLowerCase(), propertiesMap.get(properties));
        }
        System.out.println();
    }

    String propertiesString(Long n) {
        Map<String, Boolean> propertiesMap = Properties.includePropertiesMap(n);
        StringJoiner sj = new StringJoiner(", ");
        Set<String> propertiesSet = propertiesMap.keySet();
        for (String properties : propertiesSet) {
            if (propertiesMap.get(properties)) {
                sj.add(properties);
            }
        }
        return sj.toString().toLowerCase();
    }

    void multiNumbers(Long n, Long s) {
        for (int i = 0; i < s; i++) {
            System.out.printf("%d is %s\n", n, propertiesString(n));
            n++;
        }
        System.out.println();
    }

    void numbersAndProperties(Long n, Long s, String[] p) {
        int counter = 0;
        while (counter < s) {
            Map<String, Boolean> allPossibleProperties = Properties.includeAndExcludePropertiesMap(n);
            if (PropertiesCheck.checkAllProperties(allPossibleProperties, p)) {
                counter++;
                System.out.printf("%d is %s\n", n, propertiesString(n));
            }
            n++;
        }
        System.out.println();
    }
}

class BooleanCheck {

    Boolean even(Long n) {
        return n % 2 == 0;
    }

    Boolean odd(Long n) {
        return n % 2 != 0;
    }

    Boolean buzz(Long n) {
        return n % 7 == 0 || n % 10 == 7;
    }

    Boolean duck(Long n) {
        return String.valueOf(n).contains("0");
    }

    Boolean palindromic(Long n) {
        String numberToString = String.valueOf(n);
        String reverse = new StringBuffer(numberToString).reverse().toString();
        return numberToString.equals(reverse);
    }

    Boolean gapful(Long n) {
        if (!(n < 100)) {
            String numberToString = Long.toString(n);
            String firstLastDigit = numberToString.charAt(0) + String.valueOf(numberToString.charAt(numberToString.length() - 1));
            Long divisor = Long.parseLong(firstLastDigit);
            return n % divisor == 0;
        }
        return false;
    }

    Boolean spy(Long n) {
        Long product = 1L;
        Long sum = 0L;
        Long digit;

        while (n > 0) {
            digit = n % 10;
            sum = sum + digit;
            product = product * digit;
            n = n / 10;
        }
        return sum.equals(product);
    }

    Boolean square(Long n) {
        return Math.sqrt(n) - Math.floor(Math.sqrt(n)) == 0;
    }

    Boolean sunny(Long n) {
        return square(n + 1);
    }

    Boolean jumping(Long n) {
        boolean jump = true;

        while (n != 0) {
            Long digit1 = n % 10;
            n = n / 10;
            if(n != 0) {
                Long digit2 = n % 10;
                if (Math.abs(digit1 - digit2) != 1) {
                    jump = false;
                }
            }
        }
        return jump;
    }

    Boolean happy(Long n) {
        Set<Long> digits = new HashSet<>();
        while (digits.add(n)) {
            double result = 0;
            while (n > 0) {
                result += Math.pow(n % 10, 2);
                n = n / 10;
            }
            n = (long) result;
        }
        return n == 1;
    }

    Boolean sad(Long n) {
        return !happy(n);
    }
}

class Error {

    Boolean naturalCheck(String[] a) {
        boolean naturalNumber = true;
        if (a.length == 1) {
            if (a[0].contains("-")) {
                System.out.println("The first parameter should be a natural number or zero.\n");
                naturalNumber = false;
            }
        }
        else if (a.length == 2) {
            if (a[1].contains("-")) {
                System.out.println("The second parameter should be a natural number.\n");
                naturalNumber = false;
            }
        }
        else {
            if (a[0].contains("-")) {
                System.out.println("The first parameter should be a natural number or zero.\n");
                naturalNumber = false;
            }
            if(a[1].contains("-")) {
                System.out.println("The second parameter should be a natural number.\n");
                naturalNumber = false;
            }
        }
        return naturalNumber;
    }

    Boolean mutually(String[] p) {
        boolean result = false;
        List<String> properties = Arrays.asList(p);

        List<String> evenOddInclude = List.of("EVEN", "ODD");
        List<String> evenOddExclude = List.of("-EVEN", "-ODD");
        List<String> duckSpy = List.of("DUCK", "SPY");
        List<String> squareSunny = List.of("SQUARE", "SUNNY");
        List<String> happySad = List.of("HAPPY", "SAD");
        List<String> even = List.of("EVEN", "-EVEN");
        List<String> odd = List.of("ODD", "-ODD");
        List<String> duck = List.of("DUCK", "-DUCK");
        List<String> spy = List.of("SPY", "-SPY");
        List<String> square = List.of("SQUARE", "-SQUARE");
        List<String> sunny = List.of("SUNNY", "-SUNNY");
        List<String> happy = List.of("HAPPY", "-HAPPY");
        List<String> sad = List.of("SAD", "-SAD");

        List<List<String>> mutuallyList = Arrays.asList(even, odd, evenOddInclude, evenOddExclude, duckSpy, squareSunny, happySad, duck, spy, square, sunny, happy, sad);

        for (List<String> mutually : mutuallyList) {
            if (properties.containsAll(mutually)) {
                System.out.printf("The request contains mutually exclusive properties: %s\n" +
                        "There are no numbers with these properties.\n\n", mutually);
                result = true;
                break;
            }
        }
        return result;
    }
}

class Instructions {
    void printInstructions() {
        System.out.println("Welcome to Amazing Numbers!\n\n" +
                "Supported requests:\n" +
                "- enter a natural number to know its properties;\n" +
                "- enter two natural numbers to obtain the properties of the list:\n" +
                "  * the first parameter represents a starting number;\n" +
                "  * the second parameter shows how many consecutive numbers are to be processed;\n" +
                "- two natural numbers and properties to search for;\n" +
                "- a property preceded by minus must not be present in numbers;\n" +
                "- separate the parameters with one space;\n" +
                "- enter 0 to exit.\n");
    }
}
