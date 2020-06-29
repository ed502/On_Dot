<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>온점</title>
<link rel="stylesheet" href="../resources/css/style.css" />   
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="col-md-4"></div>
            <div class="col-md-4"><a href="braile.jsp"style="color: black"><h1>온점</h1></a></div>
            <div class="col-md-4"><h2>시각장애인을 위한 점자 교육 시스템</h2></div>
        </div>
        <br>
        <section class="widget">
            <div class="widget-body">
                <div class="settings-navbar">
                    <ul>
                        <li>
                            <a routerlinkactive="active" href="braile.jsp">
                                점자란?
                            </a>
                        </li>
                        <li>
                            <a routerlinkactive="active" href="wrongRanking.jsp" >
                                점자랭킹
                            </a>
                        </li>
                        <li>
                               <a routerlinkactive="active" href="translateRanking.jsp">
                                점자퀴즈결과
                            </a>
                        </li>
                        <li>
                      
                            </a>
                        </li>
                    </ul>
                    <ul style="margin-top: 36px">
                        <label>
                            <b>졸업작품 정보</b>
                        </label>
                        <li>
                            <a routerlinkactive="active" href="https://github.com/ed502/On_Dot">
                                GitHub
                            </a>
                        </li>
                        <li>
                            <a routerlinkactive="active" href="onDotInfo.jsp" class="active">
                                졸업작품 개요
                            </a>
                        </li>
                        <li>
                           <a routerlinkactive="active" href="http://www.kbuwel.or.kr/Blind/Braille">
                                점자협회
                            </a>
                        </li>
                    </ul>
                </div>
                <div class="settings-container">
                    <settings-section>
                        <div id="content" class="content SJ">
                            <div class="heading heading-depth02">
                            <h3 class="title">졸업작품 개요 - 어플리케이션</h3>
                            <br>
                            <p style="font-size: 19px;" class="desc">1) 초성 종성 모음 등 다양한 점자를 교육받을 수 있는 어플리케이션 구현 </p>
                            <p style="font-size: 19px;" class="desc">2) 점자퀴즈를 통한 학습효과의 증대</p>
                            <p style="font-size: 19px;" class="desc">3) 점자게시판을 음성으로 이용할 수 있는 기능</p>

                            <br>
                            </div>
                            <div class="box box-col01">
                                <div class="item lineX"><span><img src="./resources/img/app1.png" class="apppicture" alt="어플리케이션 화면"></span> <span><img src="./resources/img/app2.png" class="apppicture" alt=""></span> <span><img src="./resources/img/app3.png" class="apppicture" alt=""></span></div>
                                <h3 class="title">초성 종성 등 점자를 교육받는 어플리케이션의 화면</h3>
                            </div>
                            <br>
                            <div class="box box-col01">
                                <div class="item"><span><img src="./resources/img/app6.png" class="apppicture" alt=""><img src="./resources/img/app5.png" class="apppicture" alt=""><img src="./resources/img/app4.png" class="apppicture" alt=""></span></div>
                                <h3 class="title">점자퀴즈 및 점자게시판 어플리케이션 화면 </h3>
                            </div>
                            <br>
                            <h3 class="title">졸업작품 개요 - 점자키트</h3>
                            <br>
                            <p style="font-size: 19px;" class="desc">1) 점자를 직접 만지면서 배울 수 있는 점자키트의 구현 </p>
                            <p style="font-size: 19px;" class="desc">2) 최대 8개의 점자까지 확인 가능하므로 짧은 문장단위까지 인식가능</p>
                            <p style="font-size: 19px;" class="desc">3) 큰 점자판으로 확실하게 손으로 전달되는 점자의 느낌을 구현</p>
                            <br>
                            <div class="box box-col01">
                                <div class="item"><span><img src="./resources/img/kit1.png" class="kitpicture" alt=""></span></div>
                                <h3 class="title">점자키트의 모식도</h3>
                            </div>
                            <br>
                            <div class="box box-col01">
                                <div class="item"><span><img src="./resources/img/kit2.png" class="kitpicture" alt=""></span></div>
                                <h3 class="title">점자키트의 3D모델링 사진</h3>
                            </div>
                            <br>
                            <h3 class="title">졸업작품 개요 - 문제인식</h3>
                            <br>
                            <p style="font-size: 19px;" class="desc">1) 국내 시각 장애인중 86%는 점자 해독이 불가능 </p>
                            <p style="font-size: 19px;" class="desc">2) 시각장애는 95%이상이 후천적으로 발생</p>
                            <p style="font-size: 19px;" class="desc">3) 시각장애인 70%이상이 20세 이후 발생</p>
                            <p style="font-size: 19px;" class="desc">4) 글자에 익숙해진 상황에서 새롭게 점자를 습득하는 데 어려움</p>
                            <br>
                            <div class="box box-col01">
                                <div class="item"><span><img src="./resources/img/problem1.png" class="kitpicture" alt=""></span></div>
                                <h3 class="title">2017 보건복지부 장애인 실태조사 - 1</h3>
                            </div>
                            <br>
                            <div class="box box-col01">
                                <div class="item"><span><img src="./resources/img/problem2.png" class="kitpicture" alt=""></span></div>
                                <h3 class="title">2017 보건복지부 장애인 실태조사 - 2</h3>
                            </div>
                            <br>
                            <h3 class="title">졸업작품 개요 - 개발목표</h3>
                            <br>
                            <p style="font-size: 19px;" class="desc">1) 시각장애인이 혼자서도 학습을 할 수 있도록 시스템을 구성</p>
                            <p style="font-size: 19px;" class="desc">2) 시각장애인이 점자를 직접 체험할 수 있는 점자 키트 개발</p>
                            <p style="font-size: 19px;" class="desc">3) 음성인식을 통해 음성을 점자로 번역하여 점자 키트에서 표현</p>
                            <p style="font-size: 19px;" class="desc">4) 스마트폰 드래그를 통해 점자를 입력하고, 점자를 해독</p>
                            <p style="font-size: 19px;" class="desc">5) 단계별 학습을 통해 자가 학습상태 진단</p>
                            <p style="font-size: 19px;" class="desc">6) 데이터를 수집하여 시각장애인들이 어려움을 느끼는 단어 파악</p>
                        </div>
                    </settings-section>
                </div>
            </div>
        </section>
    </div>
    <div class="footer">
        <hr>
        <div>
        
        
            <div class="card">
                <div>
                    <blockquote class="blockquote mb-0">
                        <h2 style="text-align: center; margin-top: 24px !important;">Developer</h2>
                        <h2 style="text-align: center; margin-top: 24px !important;">2015154009 김학진 2015154014 문준혁 2015154021 송민욱 2015154027 이세민</h2>
                        <footer class="blockquote-footer"  style="text-align: left !important;">
                            <cite title="Source Title">한국산업기술대학교 컴퓨터공학부</cite>
                        </footer>
                    </blockquote>
                </div>
                <div class="card-header">
                    <h5 class="content">Copyright © 온점.All right reserved</h5>
                </div>
            </div>
        </div>
    </div>
</body>

</html>