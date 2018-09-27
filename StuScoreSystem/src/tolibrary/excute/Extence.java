package tolibrary.excute;

public class Extence {
	public static void main(String[] args) {
		String s = "adfs@sada@asd";
		String a = s.replace('@', ';');
		System.out.println(a);
	}
	
	//最大公约数
	public static int get_gcd(int a, int b) {
	    int max, min;
	    max = (a > b) ? a : b;
	    min = (a < b) ? a : b;
	
	    if (max % min != 0) {
	        return get_gcd(min, max % min);
	    } else
	        return min;
	
	 }

    // 最小公倍数
    public static int get_lcm(int a, int b) {
        return a * b / get_gcd(a, b);
    }

}
