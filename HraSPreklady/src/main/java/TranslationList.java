import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class TranslationList {
    private String word;
    private ArrayList<String> list;


    TranslationList(String word) throws IOException {
        this.word = word;
        this.list = makeTranslationList(word);

    }

    public List<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }


    private static ArrayList<String> makeTranslationList(String word) throws IOException, NullPointerException {

        String html0 = "https://slovnik.seznam.cz/cz-en/?q=";
        String html = html0 + word;
        Document doc = Jsoup.connect(html).get();
        Element div = doc.getElementById("fastMeanings");
        return chooseTranslations(div);
    }

    void adjustTranslationList() {
        for (int i = 0; i < this.list.size(); i++) {
            String str = this.list.get(i);
            if (str.contains("(")) {
                String newstr = str.replace("(", "");
                newstr = newstr.replace(")", "");
                this.list.set(i, newstr);
                str = str.replaceAll("\\(.*?\\)", "");
                this.list.add(str);
            } else if (str.contains("/")) {
                String[] arr = str.split("\\s");



            }
        }
    }

    private static ArrayList<String> chooseTranslations(Element div) {
        int index = 0;
        boolean isNewWord = true;
        ArrayList<String> list = new ArrayList<String>();
        if (div == null) {
            return null;
        } else {
            List<TextNode> textNodes = div.textNodes();
            Elements divElements = div.getAllElements();
            for (Element el : divElements) {
                String fullString = el.toString();
                String textNode = textNodes.get(divElements.indexOf(el)).toString();
                if (divElements.indexOf(el) == 0) {
                    continue;
                } else if (fullString.contains("<span")) {
                    String str = el.text();
                    switch (str) {
                        case ",":
                            isNewWord = true;
                            break;
                        default:
                            isNewWord = false;
                            break;
                    }
                } else if (el.toString().equals("<br>")) {
                    isNewWord = true;
                } else if (textNode.contains("/")) {
                    String str = el.text();
//                    String lastStr = list.get(index - 1)
                    if ((list.get(index - 1).charAt(list.get(index - 1).length() - 1)) == '/') {
                        list.set(index - 1, list.get(index - 1) + str + "/");
                    } else {
                        list.add(str + "/");
                        isNewWord = false;
                        ++index;
                    }
                } else {
                    String str = el.text();
                    if (isNewWord) {
                        list.add(str);
                        isNewWord = false;
                        ++index;
                    } else if ((list.get(index - 1).charAt(list.get(index - 1).length() - 1)) == '/') {
                        list.set(index - 1, list.get(index - 1) + str);
                    } else {
                        list.set(index - 1, list.get(index - 1) + " " + str);
                    }
                }
            }

        }

        return list;
    }
}
