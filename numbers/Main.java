package numbers;

import java.util.*;
import java.math.BigInteger;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Amazing Numbers!");
        printSupportMessage();

        while (true) {
            System.out.println("Enter a request:");
            String input = scanner.nextLine();
            if ("".equals(input)) {
                printSupportMessage();
                continue;
            }
            String[] strs = input.split("\\s+");
            BigInteger number = new BigInteger(strs[0]);
            if (number.compareTo(BigInteger.ZERO) < 0) {
                System.out.println("The first parameter should be a natural number or zero.");
                continue;
            }
            if (number.equals(BigInteger.ZERO)) {
                System.out.println("Goodbye!");
                break;
            }

            int repeat = 0;
            if (strs.length > 1) {
                repeat = Integer.parseInt(strs[1]);
            }  
            if (repeat < 0) {
                System.out.println("The second parameter should be a natural number.");
            }

            if (repeat == 0) {
                System.out.println("Properties of " + number);
                System.out.println("even: " + isEven(number));
                System.out.println("odd: " + isOdd(number));
                System.out.println("buzz: " + isBuzz(number));
                System.out.println("duck: " + isDuck(number));
                System.out.println("palindromic: " + isPalindromic(number));
                System.out.println("gapful: " + isGapful(number));
                System.out.println("spy: " + isSpy(number));
                System.out.println("sunny: " + isSunny(number));
                System.out.println("square: " + isSquare(number));
                System.out.println("jumping: " + isJumping(number));
                if (isHappy(number)) {
                    System.out.println("happy: true");
                    System.out.println("sad: false");
                } else {
                    System.out.println("happy: false");
                    System.out.println("sad: true");
                }
            }

            List<String> properties = new ArrayList<>();
            List<String> propertiesDeny = new ArrayList<>();
            List<String> propertyErrors = new ArrayList<>();
            for (int i = 2; i < strs.length; i++) {
                String property = strs[i];
                if (property.startsWith("-")) {
                    if (checkProperty(property.substring(1))) {
                        propertiesDeny.add(property.substring(1));
                    } else {
                        propertyErrors.add(property);
                    }   
                } else {
                    if (checkProperty(property)) {
                        properties.add(property);
                    } else {
                        propertyErrors.add(property);
                    }
                }
            }

            if (propertyErrors.size() > 0) {
                if (propertyErrors.size() == 1) {
                    System.out.println("The property [" + propertyErrors.get(0) + "] is wrong.");                       
                    System.out.println("Available properties: [BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, EVEN, ODD, SUNNY, SQUARE, JUMPING, HAPPY, SAD]");
                    continue;
                } else {
                    System.out.println("The properties [" + String.join(", ", propertyErrors) + "] are wrong.");
                    System.out.println("Available properties: [BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, EVEN, ODD, SUNNY, SQUARE, JUMPING, HAPPY, SAD]");
                    continue;
                }
            }

            List<String> mutalexErrors = checkProperties(properties, false);
            List<String> mutalexErrorsDeny = checkProperties(propertiesDeny, true);
            List<String> mutalexErrorsSame = checkSameProperties(properties, propertiesDeny);
            addDenyAll(mutalexErrorsDeny, mutalexErrors);
            addDenyAll(mutalexErrorsSame, mutalexErrors);
            if (mutalexErrors.size() > 0) {
                System.out.println("The request contains mutually exclusive properties: [" + String.join(", ", mutalexErrors) + "]");
                System.out.println("There are no numbers with these properties.");
                continue;
            }
            
            if (strs.length > 2) {
                while (repeat > 0) {
                    if (printProperty(number, properties, propertiesDeny)) {
                        repeat--;
                    }
                    number = number.add(BigInteger.ONE);
                }
            } else {
                for (int i = 0; i < repeat; i++, number = number.add(BigInteger.ONE)) {
                    printProperty(number, properties, propertiesDeny);
                }    
            }        
        }
        scanner.close();
    } 
    
    static void addDenyAll(List<String> listDeny, List<String> list) {
        for (int i = 0; i < listDeny.size(); i++) {
            list.add("-" + listDeny.get(i));
        }
    }

    static void addSameAll(List<String> listDeny, List<String> list) {
        for (int i = 0; i < listDeny.size(); i++) {
            list.add("-" + listDeny.get(i));
            list.add(listDeny.get(i));
        }
    }

    static boolean checkProperty(String property) {
        List<String> list = new ArrayList<String>(Arrays.asList("BUZZ", "DUCK", "PALINDROMIC", "GAPFUL", "SPY", "EVEN", "ODD", "SUNNY", "SQUARE", "JUMPING", "HAPPY", "SAD"));
        if (list.contains(property.toUpperCase())) {
            return true;
        } else {
            return false;
        }
    }

    static List<String> checkSameProperties(List<String> properties, List<String> propertiesDeny) {
        List<String> upperPropertiesDeny = new ArrayList<>();
        IntStream.range(0, propertiesDeny.size()).forEach(i -> upperPropertiesDeny.add(propertiesDeny.get(i).toUpperCase()));
        List<String> mutalexErrors = new ArrayList<>();
        for (int i = 0; i < properties.size(); i++) { 
            String property = properties.get(i);
            if (upperPropertiesDeny.contains(property.toUpperCase())) {
                mutalexErrors.add(property);
            }
        }
        return mutalexErrors;
    }

    static List<String> checkProperties(List<String> properties, boolean denyFlag) {
        List<String> upperProperties = new ArrayList<>();
        IntStream.range(0, properties.size()).forEach(i -> upperProperties.add(properties.get(i).toUpperCase()));
        List<String> mutalexErrors = new ArrayList<>();
        if (upperProperties.contains("EVEN") && upperProperties.contains("ODD")) {
            mutalexErrors.add("EVEN");
            mutalexErrors.add("ODD");
        }
        if (upperProperties.contains("DUCK") && upperProperties.contains("SPY")) {
            mutalexErrors.add("DUCK");
            mutalexErrors.add("SPY");
        }
        if (!denyFlag) {
            if (upperProperties.contains("SUNNY") && upperProperties.contains("SQUARE")) {
                mutalexErrors.add("SUNNY");
                mutalexErrors.add("SQUARE");
            }
        }
        if (upperProperties.contains("HAPPY") && upperProperties.contains("SAD")) {
            mutalexErrors.add("HAPPY");
            mutalexErrors.add("SAD");
        }
        return mutalexErrors;
    }

    static boolean isEven(BigInteger number) {
        if (number.remainder(new BigInteger("2")).equals(BigInteger.ZERO)) {
            return true;
        } else {
            return false;
        }
    }

    static boolean isOdd(BigInteger number) {
        if (number.remainder(new BigInteger("2")).equals(BigInteger.ONE)) {
            return true;
        } else {
            return false;
        }
    }

    static boolean isBuzz(BigInteger number) {
        if (number.remainder(new BigInteger("7")).equals(BigInteger.ZERO) || number.remainder(BigInteger.TEN).equals(new BigInteger("7"))) {
            return true;
        } else {
            return false;
        }
    }

    static boolean isDuck(BigInteger number) {
        BigInteger n = number;
        while (n.compareTo(BigInteger.ZERO) > 0) {
            if (n.remainder(BigInteger.TEN).equals(BigInteger.ZERO)) {
                return true;               
            }
            n = n.divide(BigInteger.TEN);
        }
        return false;
    }

    static boolean isPalindromic(BigInteger number) {
        BigInteger n = number;
        int t = n.toString().length();
        while (n.compareTo(new BigInteger("9")) > 0) {
            BigInteger base = BigInteger.TEN.pow(t - 1);
            BigInteger above = n.divide(base);
            BigInteger below = n.remainder(BigInteger.TEN);
            if (!above.equals(below)) {
                return false;
            }
            n = n.subtract(above.multiply(base));
            n = n.divide(BigInteger.TEN);
            t -= 2;
        }
        if (t > 1 && !n.equals(BigInteger.ZERO)) {
            return false;
        } else {
            return true;
        }
    }

    static boolean isGapful(BigInteger number) {
        if (number.compareTo(new BigInteger("100")) < 0) {
            return false;
        }
        BigInteger n = number;
        int t = n.toString().length() - 1;
        BigInteger base = BigInteger.TEN.pow(t);
        BigInteger above = n.divide(base);
        BigInteger below = n.remainder(BigInteger.TEN);
        BigInteger d = above.multiply(BigInteger.TEN).add(below); 
        if (n.remainder(d).equals(BigInteger.ZERO)) {
            return true;
        } else {
            return false;
        }   
    }

    static boolean isSpy(BigInteger number) {
        if (number.compareTo(BigInteger.TEN) < 0) {
            return true;
        }
        BigInteger product = BigInteger.ONE;
        BigInteger sum = BigInteger.ZERO;
        int size = number.toString().length();
        BigInteger n = number;
        for (int i = 0; i < size; i++) {
            BigInteger r = n.remainder(BigInteger.TEN);
            product = product.multiply(r);
            sum = sum.add(r);
            n = n.divide(BigInteger.TEN);
        }
        if (sum.equals(product)) {
            return true;
        } else {
            return false;
        } 
    }

    static boolean isSquare(BigInteger number) {
        BigInteger x = squareRoot(number);
        if (x.multiply(x).equals(number)) {
            return true;
        } else {
            return false;
        }
    }

    static boolean isSunny(BigInteger number) {
        if (isSquare(number.add(BigInteger.ONE))) {
            return true;
        } else {
            return false;
        }
    }

    static boolean isJumping(BigInteger number) {
        BigInteger n = number;
        BigInteger aft = n.remainder(BigInteger.TEN);
        n = n.divide(BigInteger.TEN);
        while (!n.equals(BigInteger.ZERO)) {
            BigInteger bef = n.remainder(BigInteger.TEN);
            n = n.divide(BigInteger.TEN);
            if (!aft.subtract(bef).abs().equals(BigInteger.ONE)) {
                return false;
            }
            aft = bef;
        }
        return true;
    }

    static boolean isHappy(BigInteger number) {
        BigInteger n = number;
        List<BigInteger> list = new ArrayList<>();
        n = sumDigitSquare(n);
        while (!n.equals(BigInteger.ONE)) {
            if (list.contains(n)) {
                return false;
            }
            list.add(n);
            n = sumDigitSquare(n);
        }
        return true;
    }

    static BigInteger sumDigitSquare(BigInteger number) {
        BigInteger n = number;
        BigInteger sum = BigInteger.ZERO;
        while (!n.equals(BigInteger.ZERO)) {
            BigInteger d = n.remainder(BigInteger.TEN);
            n = n.divide(BigInteger.TEN);
            sum = sum.add(d.multiply(d));
        }
        return sum;
    }

    static BigInteger squareRoot(BigInteger number) {
        BigInteger x0 = number;
        while (true) {
            BigInteger x1 = squareRootSeq(x0, number);
            BigInteger x2 = x1.multiply(x1);
            if (x2.equals(number)) {
                return x1;
            }
            if (x2.compareTo(number) < 0) {
                return x1;
            }
            x0 = x1;
        }
    }

    static BigInteger squareRootSeq(BigInteger x0, BigInteger a) {
        BigInteger x1 = x0.multiply(x0).add(a);
        BigInteger d = x0.multiply(new BigInteger("2"));
        x1 = x1.divide(d);
        return x1;
    }

    static void printSupportMessage() {
        System.out.println("Supported requests:");
        System.out.println("- enter a natural number to know its properties;");
        System.out.println("- enter two natural numbers to obtain the properties of the list:");
        System.out.println(" * the first parameter represents a starting number;");
        System.out.println(" * the second parameter shows how many consecutive numbers are to be printed;");
        System.out.println("- two natural numbers and properties to search for;");
        System.out.println("- a property preceded by minus must not be present in numbers;");
        System.out.println("- separate the parameters with one space;");
        System.out.println("- enter 0 to exit.");
    }

    static boolean printProperty(BigInteger number, List<String> properties, List<String> propertiesDeny) {
        List<String> lowerProperties = new ArrayList<>();
        IntStream.range(0, properties.size()).forEach(i -> lowerProperties.add(properties.get(i).toLowerCase()));
        List<String> lowerPropertiesDeny = new ArrayList<>();
        IntStream.range(0, propertiesDeny.size()).forEach(i -> lowerPropertiesDeny.add(propertiesDeny.get(i).toLowerCase()));
        List<String> list = new ArrayList<>();
        if (isEven(number)) {
            list.add("even");
        }
        if (isOdd(number)) {
            list.add("odd");
        }
        if (isBuzz(number)) {
            list.add("buzz");
        }
        if (isDuck(number)) {
            list.add("duck");
        }
        if (isPalindromic(number)) {
            list.add("palindromic");
        }
        if (isGapful(number)) {
            list.add("gapful");
        }
        if (isSpy(number)) {
            list.add("spy");
        }
        if (isSunny(number)) {
            list.add("sunny");
        }
        if (isSquare(number)) {
            list.add("square");
        }
        if (isJumping(number)) {
            list.add("jumping");
        }
        if (isHappy(number)) {
            list.add("happy");
        } else {
            list.add("sad");
        }

        if (properties.size() == 0 && propertiesDeny.size() == 0) {
            String text = number.toString() + " is " + String.join(", ", list); 
            System.out.println(text);
            return true;
        } else {
            for (int i = 0; i < lowerProperties.size(); i++) {
                if (!list.contains(lowerProperties.get(i))) {
                    return false;
                }
            }
            for (int i = 0; i < lowerPropertiesDeny.size(); i++) {
                if (list.contains(lowerPropertiesDeny.get(i))) {
                    return false;
                }
            }
            String text = number.toString() + " is " + String.join(", ", list); 
            System.out.println(text);
            return true;
        }
    }
}