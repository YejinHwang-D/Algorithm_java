package hwangyejin;
import java.util.Scanner;

public class ArrSort {

	public static void main(String[] args) {	
		for (int count = 0; count < 3; count++) {
			Scanner sc = new Scanner(System.in);
			System.out.printf("n 값 입력: ");
			int n = sc.nextInt(), temp; //n의 값 입력 받기, temp 변수 선언
			int[] arr = new int[n]; //배열 초기화
			
			System.out.printf("%d 개의 정수 입력: ", n);
			for(int i = 0 ; i < n ; i++) { //n개 만큼 정수 입력받기
				arr[i] = sc.nextInt();
			}
			
			for (int i=0; i<n; i++) { //버블 정렬
				for (int j = 1; j<n; j++) { //첫번째 index부터 비교
					if (arr[j-1] > arr [j]) { //첫번째 수가 두번째 수보다 클 때
						temp = arr[j-1]; //첫번째와 두번째 자리 교체
						arr[j-1] = arr[j];
						arr[j] = temp;
					}
				} //
			}
			System.out.printf("정렬결과 : ");
			for(int i = 0 ; i < n ; i++) { //정렬된 배열 값 출력
				System.out.printf("%d ",arr[i]);
			}
			System.out.println("\n");
		}
	}
}
