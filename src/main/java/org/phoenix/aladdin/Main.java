package org.phoenix.aladdin;

import org.phoenix.aladdin.model.entity.User;

import java.io.*;
import java.util.*;

class Grammar{
    /**
     * 存储文法描述信息
     */
    private Set<String> nonTerminalSet;//V_N
    private Set<String> terminalSet;//V_T
    private Map<String, List<List<String>>> ruleMap;//P
    private String startSymbol;//S
    //Getter与Setter
    public Map<String, List<List<String>>> getRuleMap() {
        return ruleMap;
    }

    public Set<String> getNonTerminalSet() {
        return nonTerminalSet;
    }

    public Set<String> getTerminalSet() {
        return terminalSet;
    }

    public String getStartSymbol() {
        return startSymbol;
    }

    public void setNonTerminalSet(Set<String> nonTerminalSet) {
        this.nonTerminalSet = nonTerminalSet;
    }

    public void setRuleMap(Map<String, List<List<String>>> ruleMap) {
        this.ruleMap = ruleMap;
    }

    public void setStartSymbol(String startSymbol) {
        this.startSymbol = startSymbol;
    }

    public void setTerminalSet(Set<String> terminalSet) {
        this.terminalSet = terminalSet;
    }

    //初始化文法，从文件中读取信息并构造文法
    public Grammar(File file){
        try{
            BufferedReader reader=new BufferedReader(new FileReader(file));
            Grammar.build(reader,this);//构建文法描述数据结构
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 读取文件，按输入文件格式与指定规则构建文法描述
     * @param reader 文件，文法信息的来源
     */
    public static void build(BufferedReader reader,Grammar grammar) {
        try {
            //处理非终结符集合VN
            int nonTerminalCount=Integer.parseInt(reader.readLine());
            grammar.nonTerminalSet=new HashSet<>(nonTerminalCount);
            String[] nonTerminalContent=reader.readLine().split(" ");//按空格分隔
            grammar.nonTerminalSet.addAll(Arrays.asList(nonTerminalContent));//将内容加入集合中

            //处理终结符VT
            int terminalCount=Integer.parseInt(reader.readLine());
            grammar.terminalSet=new HashSet<>(terminalCount);
            String[] terminalContent=reader.readLine().split(" ");
            grammar.terminalSet.addAll(Arrays.asList(terminalContent));

            //处理规则P
            int ruleCount=Integer.parseInt(reader.readLine());
            grammar.ruleMap=new LinkedHashMap<>();//按插入顺序有序
            for(int i=0;i<ruleCount;i++){
                String[] rule=reader.readLine().split(" ");
                List<String>rightBeta=new ArrayList<>(Arrays.asList(Arrays.copyOfRange(rule,2,rule.length)));//右部
                List<List<String>> mapRight=grammar.ruleMap.getOrDefault(rule[0],new ArrayList<>());
                mapRight.add(rightBeta);
                grammar.ruleMap.put(rule[0],mapRight);
            }

            //处理开始符S
            grammar.startSymbol=reader.readLine();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 输出文法
     * @return 文法标准描述
     */
    @Override
    public String toString() {
        StringBuilder res=new StringBuilder("文法G =（VN，VT，P，S）\n");
        //非终结符描述
        res.append("    VN={");
        for(String s:nonTerminalSet)res.append(s).append(",");
        res.deleteCharAt(res.length()-1).append("}\n");
        //终结符描述
        res.append("    VT={");
        for(String s:terminalSet)res.append(s).append(",");
        res.deleteCharAt(res.length()-1).append("}\n");
        //规则描述
        res.append("    P={");
        boolean first=true;
        for(String left:ruleMap.keySet()){
            if (!first) {
                res.append("        ");
            }
            first=false;
            res.append(left).append("->");
            for(List<String> right:ruleMap.get(left)){
                for(String rightString:right)res.append(rightString);
                res.append(" | ");
            }
            res.delete(res.length()-3,res.length()).append(",\n");
        }
        res.deleteCharAt(res.length()-1).deleteCharAt(res.length()-1).append("}\n");
        //开始符描述
        res.append("    S=").append(startSymbol);

        return  res.toString();
    }
}
public class Main {
    public static void main(String[] args){
        //File file=new File(new File("").getAbsolutePath()+"\\test.txt");
        //Grammar grammar=new Grammar(file);

        //System.out.println(grammar.toString());



    }
}