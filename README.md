# CodingJavaProject3_EasyRiskParityPortfolio (22.11.16 ~ )  
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> &nbsp; <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">  



### 목차
개발 동기 - 설계 - 후기 - 개선 사항 - 참고

## 개발 동기
- 수업시간에 MySQL을 배웠다.
- 어렸을 적부터 주식을 해왔기 때문에 그런지 MySQL을 보고 떠오른게 포트폴리오였고, 최근에 관심을 갖게 된 Ray Dalio의 투자 방식을 MySQL을 통해 간접적으로 구현해 볼 수 있을 것 같다는 생각이 들었다. 
- 그래서, "최고의 학습 방법은 직접 해보는 것이다"라는 나의 철학에 따라 프로젝트를 시작하게 됐다.

## 설계
- 배경
1) 투자에 RISK-PARITY라는 개념이 있다. 이는 Ray Dalio의 All-Weather Portfolio(또는 All-Season Portfolio)의 메인 개념이기도 한데,
다른 디테일들을 거둬내고 핵심만 본다면, 포트폴리오 내에 존재하는 자산들의 위험 비중을 동일하게 만든다는 것이다. 
쉽게 말해, 리스크가 큰 자산은 적게 투자하고, 리스크가 작은 자산은 많이 투자하여, 리스크의 비중을 동일하게 해준다는 것이다.
2) 리스크란 무엇인가? 일반적으로 잘 알려진, 다시 말해 좁은 의미의 리스크는 '손실이 발생할 가능성'이다. 하지만 넓은 의미의 리스크는 '예상과 다르게 움직일 가능성'이다.
3) 위의 내용들을 고려하여, 이 프로그램은 특정 기간 동안의 연 수익률을 소스로 하여 개별 종목이 가진 리스크를 계산해낼 것이며, 그 리스크를 가공하여 포트폴리오 내 개별 종목들의 투자 비중을 산출해낼 것이다.
4) 이를 위해 리스크를, 개별 종목의 (특정 기간 동안) 연 수익률의 표준편차 값으로 정의할 것이다. - 넓은 의미에서의 리스크에 대한 해석을 적용한 것이다.

- 기능
1) 종목 정보 입력
2) 포트폴리오에 담을 종목들을 선택하면 자동으로 관련 수리적 결과값 및 투자비중 계산

- 스키마 & 테이블 
![SQL_TABLE_ARCH](https://user-images.githubusercontent.com/109140000/202844108-d8b1074f-42ef-4238-b019-36952de3e53d.png)  
TABLE : assets  
&nbsp;&nbsp; ![assets](https://user-images.githubusercontent.com/109140000/202844471-ffc24527-1800-441d-b6b6-8381b570fbf2.png)  
TABLE : yield  
&nbsp;&nbsp; ![yield](https://user-images.githubusercontent.com/109140000/202844478-7eebd7f6-ce2c-41a8-9bcd-739e37295d2b.png)  
TABLE : yield_rate  
&nbsp;&nbsp; ![yield_rate](https://user-images.githubusercontent.com/109140000/202844483-0cb17663-6c63-442b-af25-d89b35e828a6.png)  
TABLE : stats  
&nbsp;&nbsp; ![stats](https://user-images.githubusercontent.com/109140000/202844492-ed7eaf79-b779-4eec-bd81-59aceb30710e.png)  
TABLE : portfolio  
&nbsp;&nbsp; ![portfolio](https://user-images.githubusercontent.com/109140000/202844497-2934e528-eab6-460c-9bd1-ef44d8a15886.png)  

- 사용된 쿼리문과 설명  
1) 표준편차 ( STDEV )  
SELECT   FORMAT( STDDEV_POP( ticker ) , 4 )   FROM   yield_rate   WHERE  ticker="ticker" ;  
(* 모표준편차(population standard deviation) 사용)  
2) 평균 수익률 ( AVG_Annual_Return )  
SELECT   (   
(  SELECT  EXP( AVG( LN( ticker ) ) )  FROM  yield_rate  WHERE  year<( SELECT  MAX(year)  FROM  yield_rate )  ) * ( ( COUNT(ticker) - 1 ) / COUNT(ticker) )  ) +  
(  SELECT  ticker  FROM  yield_rate  WHERE  year>=ALL(SELECT year FROM yield_rate) ) * ( 1 / COUNT( ticker )  )  
)  
FROM   yield_rate;  
(* 가장 최근 연도의 데이터 값을 따로 분리하여 계산하는 이유는, 해당 값이 고정되어 있지 않아 변동적이기 때문이다. 즉, 완전히 배제하지는 않으면서, 값의 변동에 따른 영향을 최소화하기 위함이다. )  
3) 조정 평균 수익률 ( Adjusted_AVGAR )  
SELECT   ( ((AVG_Annual_Return-1)*100) * (1-STDEV) )/100+1   FROM   stats   WHERE ticker="ticker" ;  
( * 평균 수익률에서 표준편차(오차) 만큼의 값을 감하여 그 수치를 보수적으로 조정 )  

- 프로젝트 진행 간 중점 사항  
이 프로그램의 내용은 엑셀로도 충분히 구현 가능하다. 그렇기에 엑셀 대신에 이 프로그램을 쓰는 것이 훨씬 더 효율적이라는 것이 확연히 드러나야 된다.
그래야 의미가 있다

## 후기
- SQL 역시나 어렵다. 쿼리문이 길어지고 복잡해 질수록 내가 어디서 실수했는지 찾기 어려웠다. 

- 어려웠던 부분
1) 추상적인 생각들을 객관적으로 명시화시키고, 이를 수식으로서 풀어내는 것이 어려웠다.
특히 기하평균 개념을 적용해 연 평균 수익률을 구해낼 때 많이 애먹었다. 쿼리문이 길어짐에 따라 문법에서 오류도 많이 났고, 괄호의 갯수와 위치를 확인 하는데
어려움이 컸다

## 개선 사항 
- EasyRiskParityPortfolio
1) 수익률이 음의 값을 가질 수 있다. 또한 표준편차에 따른 투자비중이 음의 값이 나올 수도 있다. 이런 경우 계산이 비정상적으로 이뤄지게 된다.
즉 음의 값이나, 지나치게 과도한 값이 나왔을 경우 처리할 수 있어야 한다.
2) 배당수익률 정보도 추가되면 좋을 것 같다

- SiHoonChris
적어도 SQL을 사용할 때는 Java 다룰 때와는 달라야 한다. 초기에 내가 Java로 프로그래밍 할 때처럼, '구체적인 설계나 계획 없이 일단 하고 보자'는 식의 방식은
SQL에서는 높은 확률로 통하지 않는다. - 물론 SQL뿐만 아니라 다른 프로그래밍 언어에서도 마찬가지지만.


## 참고
- 11-19-2022 부, 별도 관리
- 현재 Repository에서의 기록  
11-19-2022 / JavaStudy에 있던 프로그램 파일 현Repo로 이전, dbConnector 파일 추가  

- 이전 Repository : JavaStudy
- JavaStudy 내의 Commit 기록들  
(11-16-2022) &nbsp; https://github.com/SiHoonChris/JavaStudy/commit/cb2291d256063c27d5291279a469810ecc233c1f

