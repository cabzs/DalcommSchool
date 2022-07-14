<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공방 수정 폼입니다.</title>
<style type="text/css">
	
	#map{width: 100%;}
	
	#placeRoute{ height: 100px; width: 800px;}
	#etc{height: 50px; width: 800px;}
	
	.textbox{resize:none;}

</style>
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.6.0.min.js"></script>
<script src="https://polyfill.io/v3/polyfill.min.js?features=default"></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<!-- <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCmfiqODEsD_SffBbyZp3twBsE-p_brpTE&callback=initialize&v=weekly&region=KR" defer></script> -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/googlemap-api.js"></script>

<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">

<script type="text/javascript">

function sample6_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if(data.userSelectedType === 'R'){
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraAddr !== ''){
                    extraAddr = ' (' + extraAddr + ')';
                }
                // 조합된 참고항목을 해당 필드에 넣는다.
                document.getElementById("sample6_extraAddress").value = extraAddr;
            
            } else {
                document.getElementById("sample6_extraAddress").value = '';
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('sample6_postcode').value = data.zonecode;
            document.getElementById("sample6_address").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("sample6_detailAddress").focus();
        }
    }).open();
}
</script>
	
<script type="text/javascript">

/*
공방 인프라 리스트 가져오기 
*/
	
$(function(){

	function selectPlaceInfra(){
		$.ajax({
			url:"/teacher/teacherMypage/place/selectPlaceInfra",
			type: "post",
			data : {"${_csrf.parameterName}": "${_csrf.token}"},
			dataType: "json",
			success: function(result){
//					swal(result);
				text=""
				$.each(result, function(index,item){
					text += `<span><input type="checkbox" name='infraId' id='\${item.infraId}' value='\${item.infraId}'>&nbsp\${item.infraName}</span>`;
				})
				$("#placeInfra").html(text);

				selectMyInfra();
			},
			error: function(err){
				swal("인프라 정보를 가져올 수 없습니다.")
			}
		})
	}
	
	function selectMyInfra() {
		$.ajax({
			url:"${pageContext.request.contextPath}/teacher/teacherMypage/place/selectInfra",
			type: "post",
			data : {"${_csrf.parameterName}": "${_csrf.token}", "placeId": ${place.placeId != null?place.placeId:0}},
			dataType: "json",
			success: function(result){
				if(result != null){
					$.each(result, function(index,item){
						$("#" + item.infraId).prop("checked", true);
					})
				}
			},
			error: function(err){
				swal("인프라 정보를 가져올 수 없습니다.")
			}
		})
	}
	selectPlaceInfra();
})

/*
공방 지역 정보 가져오기
*/
$(function(){
	function selectPlaceRegion(){
		$.ajax({
			url: "${pageContext.request.contextPath}/place/selectPlaceRegion",
			type: "post",
			data: {"${_csrf.parameterName}": "${_csrf.token}"},
			dataType: "json",
			success: function(result){
				text = ""
				$.each(result, function(index, item){
					if(item.regionId == ${place.placeRegion.regionId!= null?place.placeRegion.regionId:0}){
						text += `<option selected value='\${item.regionId}'>\${item.regionName}</option>`;
					} else {
						text += `<option value='\${item.regionId}'>\${item.regionName}</option>`;
					}
				})
				text += ""
				$("select[name = placeRegion]").append(text);
			},
			error: function(err){
				
				swal("지역정보를 가져올 수 없습니다.")
			}
		})
	}

	selectPlaceRegion();
})
	 
	
	var geocoder;
	  var map;
	  function initialize() {
	    geocoder = new google.maps.Geocoder();
	    var latlng = new google.maps.LatLng(37.534089572097, 127.1450466624);
	    var mapOptions = {
	      zoom: 14,
	      center: latlng
	    }
	    map = new google.maps.Map(document.getElementById('map'), mapOptions);
	  }
	  
	$(function(){
		  $("#sample6_detailAddress").focus(function(){
			  codeAddress();
		  })
	})	  
	  function codeAddress() {
			    var address = document.getElementById('sample6_address').value;
			    geocoder.geocode( { 'address': address}, function(results, status) {
			      if (status == 'OK') {
			        map.setCenter(results[0].geometry.location);
			        var marker = new google.maps.Marker({
			            map: map,
			            position: results[0].geometry.location
			        });
			      } else {
			        swal('Geocode was not successful for the following reason: ' + status);
			      }
			    });
	  }
	
// 	function checkInfra(){
// 		if()
// 	}
	
</script>
</head>
<body>

  <form id="updateForm" method="post" action="${pageContext.request.contextPath}/place/update">
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
  <input type="hidden" name="placeId"  value="${place.placeId}">
<%--   <input type="hidden" name="teacherId" value="${teacherId}"> --%>
    <table id="classTable">
      <tr>
      	<td>
        	<div class="form-floating mb-3">
				<input type="text" class="form-control" id="placeName" name="placeName" value="${place.placeName}" placeholder="사용하시는 공방의 이름을 설정해주세요.">
				<label for="placeName">공방 이름</label>
			</div>
        </td>
      </tr>
      <tr>
<!--         <th>공방 지역</th> -->
        <td>
        	<div class="mb-3">
	         	<select name="placeRegion" class="form-select" aria-label="Default select example" required>
	            	<option value="0">공방 지역 선택</option>
	          	</select>
          	</div>
        </td>
      </tr>
      <tr>
<!-- 		<th>공방 주소</th> -->
		<td>
			<input type="text" id="sample6_postcode" class="form-control" readonly="readonly" required hidden>
<!-- 			<input type="button" onclick="sample6_execDaumPostcode()" class="btn btn-outline-dark shadow-none" value="주소 검색"> -->
			<div class="form-floating mb-3"><input type="text" id="sample6_address" name="placeAddr" class="form-control" onclick="sample6_execDaumPostcode()" value="${place.placeAddr}" readonly="readonly" required>
			<label for="placeAddr">공방 주소</label></div>
			<input type="text" id="sample6_detailAddress" name="detailAddr" class="form-control" value="${place.detailAddr }"placeholder="상세주소1(선택)">
			<input type="text" id="sample6_extraAddress" class="form-control" placeholder="상세주소2(선택)" hidden>
	  	</td>
	  </tr>
	  <tr>
	  	<td>
			<div id="map" class="form-floating mb-3"></div>
		</td>
	  </tr>
	  <tr>
<!-- 	    <th>찾아오는 방법</th> -->
	    <td>
<%-- 	    <input type="text" name="placeRoute" value="${place.placeRoute}"> textarea는 value 속성이 적용되지 않는다. 대신 바깥에 적는다. --%>
			<div class="form-floating mb-3">
	    	<textarea id="placeRoute" name="placeRoute" class="form-control textbox" placeholder="공방을 찾아오는 방법을 간단하게 설명해주세요.&#13;&#10; ex)강동역에 내려서 341번 버스를 타고 길동사거리 앞에서 내리시면 됩니다^^">${place.placeRoute}</textarea>
	    	<label for="placeRoute">찾아오는 방법</label>
	    	</div>
	    </td>
	  </tr>     
      <tr>
<!--       공방 편의 시설 -->
      	<td>
      		<fieldset>
      			<div class="form-floating mb-3" id="placeInfra">
<!--         		<label for="placeInfra">*공방 편의 시설*</label> -->
            	</div>
            </fieldset>
        </td>
      </tr>
      <tr>
        <td>
        	<div class="d-grid gap-2">
        	  <input type="submit" class="btn btn-primary" value="수정">
        	</div>
        </td>
      </tr>
    </table>
  </form>

</body>
</html>