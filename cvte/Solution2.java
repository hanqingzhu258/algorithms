package cvte;

import java.util.*;

/**
 * @projectName: mianshi
 * @packageName: cvte
 * @Author: hanqing zhu
 * @Date: 19:46 2018/9/19
 * @Description: 将目标正整数分解集合内元素的任意组合
 */
public class Solution2 {

    //存放每个每个整数的分解情况
    public static Map<Integer, List<List<Integer>>> numberMap = new HashMap<>();

    //存放组合结果
    public static List<List<Integer>> result = new ArrayList<>();

    //已知元素集合
    public static List<Integer>  initNumbers = Arrays.asList(3,4, 5,6,9,12);

    //主程序
    public static void main(String[] args) {

        //目标整数值
        int aim = 12;

        //1.首先对已知集合的所有元素从小到大排序
        Collections.sort(initNumbers, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });

        //2.开始正式操作
        init(initNumbers,aim);

        //递归查找
        findCollections(-1,-1,aim,result,initNumbers);

        //对numberMap进行重新解析
        resolveNumberMap(numberMap,initNumbers,aim);

        //删除其余可能存在错误的解
        removeErrorResults(result,aim);

        //对现有结果进行后续处理
        handleResult(result,numberMap);

        //删除其余可能存在错误的解
        removeErrorResults(result,aim);

        //添加元素本身的操作
        if (initNumbers.contains(aim)){
            result.add(new ArrayList<>(Arrays.asList(aim)));
        }

        //结果输出
        printNumberMap(numberMap);
        printResult(result);


    }

    /**
     * @Author: hanqing zhu
     * @Date: 21:06 2018/9/19
     * @Return:
     * @Description: 初始化map
     */
    public static void init(List<Integer> numbers,int aim) {
        for (int i=1;i<=aim;i++){
            List<List<Integer>> lists=new ArrayList<>();
            numberMap.put(new Integer(i),lists);
        }
    }

    /**
     * @param n1             :分解参数之一，默认为-1
     * @param n2             :分解参数之一，默认为-1
     * @param ob             :目标整数值
     * @param result         :结果集合
     * @param numbers:已知元素集合
     * @Author: hanqing zhu
     * @Date: 19:56 2018/9/19
     * @Return:
     * @Description:
     */
    public static void findCollections(int n1, int n2, int ob, List<List<Integer>> result, List<Integer> numbers) {

        //找出已知集合中所有小于目标元素的集合
        List<Integer> lns = new ArrayList<>();
        for (Integer num : numbers) {
            if (num < ob) {
                int n=num.intValue();
                lns.add(n);
            }
        }

        if (lns.size()==0){
            return;
        }

        //从已知集合中选取两个值，分别赋予n1和n2
        //n1选取当前最小的
        int left;
        left= lns.get(0);
        //如果left值超过目标值的一半，则无需重新

        int right = ob - left;


            //将此两个元素的集合作为一个结果，并保存
            List<Integer> re = new ArrayList<>(Arrays.asList(left, right));

            /*if (initNumbers.contains(ob)) {*/
                //获取当前分解的目标值对应的分解集合
                List<List<Integer>> obList = numberMap.get(ob);
                //添加刚才对应的集合
                addResult(obList,Arrays.asList(left, right));
            /*}*/
            //将刚才选中的两个元素从lns集合中删除
            lns.remove(new Integer(left));
            lns.remove(new Integer(right));


            //分解的父右边元素
            if (n1 != -1) {
                re.add(n1);
                addResult(result,re);
                //如果当前目标值的分解组合已寻找完毕
                if (lns.size()==0){
                    findCollections(left,-1,right,result,initNumbers);
                }else{
                    //继续以该目标值进行分解，不过已知元素集合变为lns
                    findCollections(n1, -1, ob, result, lns);
                }

            }
            //分解的是父左边元素
            if (n2 != -1) {
                re.add(n2);
                addResult(result,re);
                findCollections(-1, n2, ob, result, lns);
            }
            //如果是初始值
            if (n1 == -1 && n2 == -1) {
                addResult(result,re);
                //对左右两侧的值分别进行分解
                //对左边的值进行分解
                findCollections(-1, right, left, result, initNumbers);
                //对右边的值进行分解
                findCollections(left, -1, right, result, initNumbers);

                //进行一下轮
                findCollections(-1,-1,ob,result,lns);

            }


    }

    /**
     * @Author: hanqing zhu
     * @Date: 9:53 2018/9/20
     * @Return:
     * @param results :结果集合
     * @param result :目标集合
     * @Description: 避免向result中重复添加的方法
     */
    public static void addResult(List<List<Integer>> results,List<Integer> result){

        if (results.size()==0){
            results.add(result);
            return;
        }

        int count=0;

        for (List<Integer> re:results){
            if (twoListCompareUtil(re,result)){
                count++;
            }
        }

        if (count==0){
            results.add(result);
        }

    }

    /**
     * @Author: hanqing zhu
     * @Date: 9:53 2018/9/20
     * @Return:
     * @param firstList
     * @param scList
     * @Description: 比较两个list中的值是否相等
     */
    public static boolean twoListCompareUtil(List<Integer> firstList, List<Integer> scList) {
        //首先判断这两个list的大小是否相等
        if(firstList.size() != scList.size()){
            return false;
        }
        //如果两个list大小相等，其中可能为0，所以排除为0的这种情况
        if(firstList.size()>0 && scList.size()>0){
            //如果相等，对两个list分别排序以方便后期比较
            Collections.sort(firstList, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1-o2;
                }
            });
            Collections.sort(scList, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1-o2;
                }
            });
            //循环遍历两个几个取值比较是否相同，不同则返回false
            for(int i=0;i<firstList.size();i++){
                //取值比较是否相同
                if(!firstList.get(i).equals(scList.get(i))){
                    return false;
                }
            }
            return true;
        }else{
            return false;
        }

    }

    /**
     * @Author: hanqing zhu
     * @Date: 9:52 2018/9/20
     * @Return:
     * @param list
     * @Description: 快速进行list的输出
     */
    public static void fastPrint(List<Integer> list){
        String result="";
        for (Integer integer:list){

            result+=integer+",";

        }
        System.out.println(result);
    }

    /**
     * @Author: hanqing zhu
     * @Date: 10:36 2018/9/20
     * @Return:
     * @param result
     * @Description: 输出结果
     */
    public static void printResult(List<List<Integer>> result){
        System.out.println("result:");
        for (List<Integer> result1:result){
            for (Integer num:result1){
                System.out.print(num+",");
            }
            System.out.println("");
        }
    }

    /**
     * @Author: hanqing zhu
     * @Date: 10:38 2018/9/20
     * @Return:
     * @param numberMap
     * @Description: 输出map
     */
    public static void printNumberMap(Map<Integer, List<List<Integer>>> numberMap){
        System.out.println("numberMap:");
        for (Map.Entry<Integer,List<List<Integer>>> entry:numberMap.entrySet()){
            System.out.println(entry.getKey()+":");
            for (List<Integer> list:entry.getValue()){
                for (Integer num:list){
                    System.out.print(num+",");
                }
                System.out.println("");
            }
        }
    }

    /**
     * @Author: hanqing zhu
     * @Date: 10:40 2018/9/20
     * @Return:
     * @param result
     * @param numberMap
     * @Description: 对结果进行后续处理，将可以分解的结果进行进一步分解组合
     */
    public static void handleResult(List<List<Integer>> result,Map<Integer, List<List<Integer>>> numberMap){

        //新结果集
        List<List<Integer>> newResults=new ArrayList<>();

        //对原结果集中的只有两个数相加的解，进行分解组合
        for (List<Integer> re:result){
            if (re.size()==2){

                for (List<Integer> r1:numberMap.get(re.get(0))){
                    for (List<Integer> r2:numberMap.get(re.get(1))){
                        List<Integer> newRe=new ArrayList<>();
                        newRe.addAll(r1);
                        newRe.addAll(r2);
                        newResults.add(newRe);
                    }
                }

            }
        }

        //添加新解
        for (List<Integer> nr:newResults){
            addResult(result,nr);
        }


    }

    /**
     * @Author: hanqing zhu
     * @Date: 11:27 2018/9/20
     * @Return:
     * @param numberMap
     * @param initNumbers
     * @Description: 从小到大，对每一个元素再次进行分解，且分解完全
     */
    public static void resolveNumberMap(Map<Integer, List<List<Integer>>> numberMap,List<Integer> initNumbers,Integer aim){

        //对map中的每一个元素进行终极分解
        for (Integer num:initNumbers) {

            if (num>=aim){
                break;
            }

            List<List<Integer>> eles=numberMap.get(num);
            List<List<Integer>> newEles=new ArrayList<>();

            if (eles.size()==0){
                continue;
            }

            //对于当前元素的每一个分解方式（初始均是两个值的和）
            for (List<Integer> ele:eles){

                Integer e1=ele.get(0);

                List<List<Integer>> l1=new ArrayList<>();
                List<List<Integer>> e1Lists=numberMap.get(e1);
                l1.addAll(e1Lists);
                if (initNumbers.contains(e1)){
                    List<Integer> e10=Arrays.asList(e1);
                    l1.add(e10);
                }

                Integer e2=ele.get(1);
                List<List<Integer>> l2=new ArrayList<>();
                List<List<Integer>> e2Lists=numberMap.get(e2);
                l2.addAll(e2Lists);
                if (initNumbers.contains(e2)){
                    List<Integer> e20=Arrays.asList(e2);
                    l2.add(e20);
                }

                combineNewResult(newEles,l1,l2);

            }

            //将新的分解方式添加到原先的集合中
            for (List<Integer> re:newEles){
                addResult(eles,re);
            }

            newEles.clear();

            //遍历结果集合，观测是否有组合情况被遗漏
            for (List<Integer> re:eles){
                if (re.size()>3){

                    for (int i=0;i<re.size();i++){
                        for (int j=0;j!=i&&j<re.size();j++){
                            List<Integer> newRe=new ArrayList<>();
                            Integer sum=re.get(i)+re.get(j);
                            if (initNumbers.contains(sum)){
                                newRe.add(sum);
                                newRe.addAll(re);
                                /*newRe.removeAll(Arrays.asList(re.get(i),re.get(j)));*/
                                newRe.remove(re.get(i));
                                newRe.remove(re.get(j));
                                addResult(newEles,newRe);
                            }
                        }
                    }
                }
            }

            //将新的分解方式添加到原先的集合中
            for (List<Integer> re:newEles){
                addResult(eles,re);
            }

            if (maxN(initNumbers,num,aim)){
                addResult(eles,new ArrayList<>(Arrays.asList(num)));
            }

        }

    }

    public static boolean maxN(List<Integer> initNumbers,Integer num,Integer aim){

        boolean state=true;

        for (Integer n:initNumbers){
            if (n>num&&n<aim){
                state=false;
            }
        }

        return state;
    }

    /**
     * @Author: hanqing zhu
     * @Date: 13:09 2018/9/20
     * @Return:
     *
     * @Description: 组合新解
     */
    public static void combineNewResult(List<List<Integer>> newResult,List<List<Integer>> e1Lists,List<List<Integer>> e2Lists){

        for (List<Integer> e1List:e1Lists){
            for (List<Integer> e2List:e2Lists){

                List<Integer> newResolution=new ArrayList<>();
                newResolution.addAll(e1List);
                newResolution.addAll(e2List);
                addResult(newResult,newResolution);

            }
        }

    }

    /**
     * @Author: hanqing zhu
     * @Date: 10:59 2018/9/20
     * @Return:
     * @param numberMap
     * @Description: 判断已知集合中的某个元素是否可以被分解
     */
    public static boolean ifElementResolvable(Integer ele,Map<Integer, List<List<Integer>>> numberMap){
        List<List<Integer>> lists=numberMap.get(ele);
        if (lists.size()==0){
            return false;
        }
        return true;
    }

    /**
     * @Author: hanqing zhu
     * @Date: 13:44 2018/9/20
     * @Return:
     *
     * @Description: 删除其余可能存在错误的解
     */
    public static void removeErrorResults(List<List<Integer>> result,Integer aim){
        List<List<Integer>> removeResults=new ArrayList<>();
        //删除其余可能存在错误的解
        for (List<Integer> re:result){
            int sum=0;
            for (Integer e:re){
                sum+=e;
            }
            if (sum!=aim){
                removeResults.add(re);
            }
        }
        result.removeAll(removeResults);

        removeResults.clear();

        for (List<Integer> re:result){
            for (int i=1;i<aim;i++){
                if (re.contains(i)&&!initNumbers.contains(new Integer(i))){
                    removeResults.add(re);
                    break;
                }
            }
        }

        result.removeAll(removeResults);
    }
}
