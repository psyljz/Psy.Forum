package psycholabs.Forum.utli;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    private static final String REPLACEMENT ="***";
    private TrieNode rootNode= new TrieNode();


    //调用构造器之后就
    @PostConstruct
    public void init(){


        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("SenstiveWord.txt");
             BufferedReader reader =new BufferedReader(new InputStreamReader(is))
        ){
            String keyword;
            while ((keyword=reader.readLine())!=null) {

                //添加到前缀树
                this.addKeyword(keyword);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }



    //将一个铭敏感词添加到前缀树
    private void addKeyword(String keyword){

        TrieNode tempNode=rootNode;
        for (int i=0;i<keyword.length();i++){
            char c =keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);

            if (subNode==null){
                subNode = new TrieNode();
                tempNode.addSubNode(c,subNode);
            }

            // 指向子节点 进入下一轮循环
            tempNode =subNode;

            //
            if (i==keyword.length()-1){
                tempNode.setKeywordEnd(true);
            }

        }

    }

    /**
     *
     * @param text 带过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text){
        System.out.println("开始过滤");
        if (StringUtils.isBlank(text)){
            return null;
        }

        //指针 1
        TrieNode tempNode =rootNode;

        //指针2
        int begin=0;
        int position=0;
        // 结果
        StringBuilder sb =new StringBuilder();

        while (position<text.length()){
            char c= text.charAt(position);
            //跳过符号
            if (isSymbol(c)){
                //若指针1处于根节点，将此符号计入结果，让指针2向下走一步
                if (tempNode ==rootNode){
                    sb.append(c);
                    begin++;
                }

                //无论符号在开头或中间，指针3都向下走一步
                position++;
                continue;
            }

            tempNode=tempNode.getSubNode(c);
            if (tempNode ==null){
                //以begin开头的字符不是敏感词
                sb.append(text.charAt(begin));
                //进入下一个位置
                position = ++begin;
                //重新指向根节点
                tempNode=rootNode;

            }else if (tempNode.isKeywordEnd()){
                //发现敏感词begin-position字符串替换掉
                sb.append(REPLACEMENT);
                begin=++position;
                //
                tempNode =rootNode;

            }
            else {
                position++;
            }

        }
        //将最后一批计入记过
        sb.append(text.substring(begin));
        return sb.toString();
    }

    // 判断是否为符号
    private boolean isSymbol(Character c){

        //0x2E80 -0x9FFF东亚符号范围
        return !CharUtils.isAsciiAlphanumeric(c)&&(c<0x2E80|| c>0x9FFF);
    }

    //前缀树
    private class TrieNode{

        //关键词结束的标识
        private boolean isKeywordEnd = false;

        // 子节点(key是下级字符，value是下级节点)
        private Map<Character,TrieNode> subNodes =new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }
        public void addSubNode(Character c,TrieNode node){
            subNodes.put(c,node);
        }
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }
}
