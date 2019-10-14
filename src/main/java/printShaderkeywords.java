public class printShaderkeywords {
    public static void main(String[] args){
        String pragma = "#pragma multi_compile";

        int i = 0;
        while (i < 256){
            System.out.println(pragma + " _a" + i + " _b" + i);
            i++;
        }
    }
}
