<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<script src="import/scrollDiv.js"></script>


<c:set var="company">
	<spring:eval expression="@propertyConfigurer.getProperty('mfms2.company')" />
</c:set>
<header class="main-header">

	<c:choose>
		<c:when test="${currRole.siteKey == 1}">
			<c:set var="siteSelected" value="false" />
		</c:when>
		<c:when test="${empty currRole}">
			<c:set var="siteSelected" value="false" />
		</c:when>
		<c:otherwise>
			<c:set var="siteSelected" value="true" />
		</c:otherwise>
	</c:choose>

	<!-- Header Navbar: style can be found in header.less -->
	<nav class="navbar navbar-static-top" role="navigation">
		<sec:authorize access="isAuthenticated()">
			<!-- Sidebar toggle button-->
			<a href="#" class="sidebar-toggle" data-toggle="offcanvas"
				role="button"> <span class="sr-only">Toggle navigation</span>
			</a>
		</sec:authorize>
		<!-- Navbar Right Menu -->

		<div class="hidden-xs">
			<ul class="nav navbar-nav">
				<li id="titileImage">
					<c:if test="${siteSelected == 'true'}">
						<c:choose>
						     <c:when test="${company=='ebsl'}">          
								<a href="Home.do"> <span><img
										src="import/img2/logo_Cyberport.png"  width="100px"
										style="vertical-align: bottom;" alt="">&nbsp;<spring:message
											code="title.name" /></span>
							</a>
						     </c:when>
						     <c:when test="${company=='jec'}">          
								<a href="Home.do"> <span><img
										src="import/img2/logo_mfms_jec.png"
										style="vertical-align: bottom;" alt="">&nbsp;<spring:message
											code="title.name.jec" /></span>
								</a>
						     </c:when>
						     <c:when test="${company=='mss'}">          
								<a href="Home.do"> <span><img
										src="import/img2/logo_mss_s.png"  
										style="" alt="">&nbsp;<spring:message
											code="title.name.mss" /></span>
								</a>
						     </c:when>
						     <c:when test="${company=='csl'}">          
								<a href="Home.do"> <span><img
										src="import/img2/logo_1010_s.png"
										style="vertical-align: bottom;" alt=""></span>
							</a>
							</c:when>
							<c:otherwise> 
								<a href="Home.do"> <span><img
										src="import/img2/logo_Cyberport.png"  width="100px"
										style="vertical-align: bottom;" alt="">&nbsp;<spring:message
											code="title.name" /></span>
							</c:otherwise>
						</c:choose>						
					</c:if> 
					
					<c:if test="${siteSelected == 'false'}">
						<c:choose>
						     <c:when test="${company=='ebsl'}">          
								<a class="disabled" href="#"> <span><img
										src="import/img2/logo_mfms_s.png"  width="60px"
										style="vertical-align: bottom;" alt="">&nbsp;<spring:message
											code="title.name" /></span>
								</a>
						     </c:when>
						     <c:when test="${company=='jec'}">          
								<a href="Home.do"> <span><img
										src="import/img2/logo_mfms_jec.png"
										style="vertical-align: bottom;" alt="">&nbsp;<spring:message
											code="title.name.jec" /></span>
								</a>
						     </c:when>
						     <c:when test="${company=='mss'}">          
								<a class="disabled" href="#"> <span><img
										src="import/img2/logo_mss_s.png"
										style="" alt="">&nbsp;<spring:message
											code="title.name.mss" /></span>
								</a>
						     </c:when>
						     <c:when test="${company=='csl'}">          
								<a class="disabled" href="#"> <span><img
										src="import/img2/logo_1010_s.png"
										style="vertical-align: bottom;" alt=""></span>
								</a>
						     </c:when>
						     <c:otherwise> 
						     	<a class="disabled" href="#"> <span><img
										src="import/img2/logo_mfms_s.png"  width="60px"
										style="vertical-align: bottom;" alt="">&nbsp;<spring:message
											code="title.name" /></span>
								</a>
						     </c:otherwise>
						</c:choose>						
					</c:if>
				</li>
			</ul>
		</div>
		<div class="navbar-custom-menu">
			<sec:authorize access="isAuthenticated()">
				<c:if test="${siteSelected == 'true'}">
					<ul class="nav navbar-nav">
						<li id="titileUserName"><a href="ViewOwnAccount.do"><i
								class="icon-icon_account_b"></i>&nbsp;<span
								class=""><c:out value="${user.name}" /></span>
							<%-- 	class="hidden-sm hidden-xs"><c:out value="${user.name}" /></span> --%>
								<c:if test="${not empty currRole}">
									<span class="hidden-md hidden-sm hidden-xs">(<c:out
											value="${currRole.site.name}" />)
									</span>
								</c:if> </a></li>
					</ul>
					<ul class="nav navbar-nav">
						<li style="padding-top: 22px">|</li>
					</ul>
				</c:if>
				<ul class="nav navbar-nav">
					<li><a href="j_spring_security_logout"><i
							class="icon-icon_logout_b"></i></a></li>
				</ul>
			</sec:authorize>
			<ul class="nav navbar-nav">
				<li style="padding-top: 22px">|</li>
			</ul>
			<ul class="nav navbar-nav">
				<li><a href="#" onclick="changeLocale('en')"><spring:message
							code="navigation.language.en" /></a></li>
			</ul>
			<ul class="nav navbar-nav">
				<li style="padding-top: 22px">|</li>
			</ul>
			<ul class="nav navbar-nav">
				<li><a href="#" onclick="changeLocale('zh_hk')"><spring:message
							code="navigation.language.zh-hk" /></a></li>
			</ul>
		</div>
	</nav>
	<div style="height: 10px;">
		<table width="100%">
			<tr>
				<td width="255px" height="10px" bgcolor="#24A12B"></td>
				<td height="10px" bgcolor="#0D5185"></td>
			</tr>
		</table>
	</div>
	<script>
		function changeLocale(value) {
			key = "lang";
			value = encodeURI(value);

			var kvp = document.location.search.substr(1).split('&');

			var i = kvp.length;
			var x;
			while (i--) {
				x = kvp[i].split('=');

				if (x[0] == key) {
					x[1] = value;
					kvp[i] = x.join('=');
					break;
				}
			}

			if (i < 0) {
				kvp[kvp.length] = [ key, value ].join('=');
			}

			//this will reload the page, it's likely better to store this until finished
			document.location.search = kvp.join('&');
		}
		
		//var scrollDivWidth = '<spring:eval expression="@propertyConfigurer.getProperty('browser.width')" />';
		var scrollDivWidth = 850;

		$( document ).ready(function() {
			headerWarper();
			//use a div to wrap the table and set the mini-width. 
			tableWraper(scrollDivWidth);
			
		    var sw = $("#example1_wrapper").width();
		    //determine whether the scrollDiv auto overflow-x or not.
		    scrollDiv(sw-30, scrollDivWidth);
			
			$( window ).resize(function() {			
				//$("#example1").css("min-width",scrollDivWidth+"px");
				 sw2 = $("#scrollDiv").width();
				 scrollDiv(sw2, scrollDivWidth);	
				 
				 //for main header element appearance
				 mh = $(".main-header").width();
				 //console.log("mh: " + mh);
				 if(mh<=800){
					 $("#titileImage").hide();
				 } else{
					 $("#titileImage").show();
				 }	
				
				//for the appearance of both the login page and site select page
				var tun = $("#titileUserName").width()
				if(tun != null){
					//for pc 
					$(".main-header").css("min-width","420px");
				} else{
					//for phone
					$(".main-header").css("min-width","360px");
				}					
			});
		});
		
	</script>
</header>
