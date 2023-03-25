package hw3;

import java.util.Map;

import static java.util.Map.entry;

public class TransliteratorImpl implements Transliterator {
    private final Map<Character, String> transliterationRules = Map.ofEntries(entry('А', "A"),
            entry('Б', "B"), entry('В', "V"), entry('Г', "G"), entry('Д', "D"),
            entry('Е', "E"), entry('Ё', "E"), entry('Ж', "ZH"), entry('З', "Z"),
            entry('И', "I"), entry('Й', "I"), entry('К', "K"), entry('Л', "L"),
            entry('М', "M"), entry('Н', "N"), entry('О', "O"), entry('П', "P"),
            entry('Р', "R"), entry('С', "S"), entry('Т', "T"), entry('У', "U"),
            entry('Ф', "F"), entry('Х', "KH"), entry('Ц', "TS"), entry('Ч', "CH"),
            entry('Ш', "SH"), entry('Щ', "SHCH"), entry('Ы', "Y"), entry('Ь', ""),
            entry('Ъ', "IE"), entry('Э', "E"), entry('Ю', "IU"), entry('Я', "IA")
    );

    @Override
    public String transliterate(String source) {
        if (source == null) {
            return "";
        }
        StringBuilder transliteratedString = new StringBuilder();
        for (Character character : source.toCharArray()) {
            if (transliterationRules.containsKey(character)) {
                transliteratedString.append(transliterationRules.get(character));
            } else {
                transliteratedString.append(character);
            }
        }
        return transliteratedString.toString();
    }
}
