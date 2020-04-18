<!DOCTYPE html>
<html>
<head>
    <title>Gutschein</title>
    <meta charset="utf-8">
    
    <link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css">

    <link href="https://fonts.googleapis.com/css?family=Open+Sans:600" ; rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:700" ; rel="stylesheet">
    
    <link href="https://fonts.googleapis.com/css?family=Roboto+Mono:700" ; rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Roboto+Mono:700" ; rel="stylesheet">

    <style type="text/css" media="all">
        /* http://meyerweb.com/eric/tools/css/reset/
        v2.0 | 20110126
        License: none (public domain) -- my -version
        */
        html, body, div, span, applet, object, iframe, h1, h2, h3, h4, h5, h6, p, blockquote, pre, a, abbr, acronym, address, big, cite, code, del, dfn, em, img, ins, kbd, q, s, samp, small, strike, strong, sub, sup, tt, var, b, u, i, center, dl, dt, dd, ol, ul, li, fieldset, form, label, legend, table, caption, tbody, tfoot, thead, tr, th, td, article, aside, canvas, details, embed, figure, figcaption, footer, header, hgroup, menu, nav, output, ruby, section, summary, time, mark, audio, video {
            margin: 0;
            padding: 0;
            border: 0;
            vertical-align: baseline;
            text-align: centered;
        }

        /* HTML5 display-role reset for older browsers */
        article, aside, details, figcaption, figure, footer, header, hgroup, menu, nav, section {
            display: block;
        }

        body {
            line-height: 1;
        }


        html, body {
            width: 595px !important;
            margin: 0;
            padding: 0;
            
            font-family: OpenSans;
        }
        
        
        /* Sections */

        .dina4 {
            width: 555px !important;
            padding: 0 0 0 0;
            position: relative;
        }
        
        .upper-part .lower-part {
            position: relative;
            width: 100%;
        }
        
        .lower-part {
            background-color: #2397d4;
        }
        
        .header {
            margin-top: 12px;
            height: 112px !important;
            position: relative;
            width: 100%;
        }
        
        .header .left {
            position: absolute;
            left: 0;
            top: 0;
            width: 156px;
            height: 104px;
        }
        
        .header .center {
            position: absolute;
            top: 0;
            left: 172px;
            right: 172px;
            height: 104px;
        }
        
        .header .right {
            position: absolute;
            right: 0;
            top: 0;
            width: 156px;
            height: 104px;
        }
        
        .header .header-text {
            text-align: center;
        }
        
        .footer {
            position: relative;
            width: 100%;
            height: 42px;
            padding-bottom: 20px; 
        }

        .footer .left {
            position: absolute;
            top: 0;
            left: 12px; 
            width: 145px;
            
            font-size: 9px;
            text-align: right;
            line-height: 10px;
            
            color: rgba(254, 254, 254, 0.87);
        }
        
        .footer .center {
            position: absolute;
            top:0;
            left: 169px;
            right: 185px;
            background-color: #ffffff;
            height: 42px;
        }
        
        .footer .center .code {
          width: 100%;
          height: 42px;
          line-height: 42px;
          font-family: RobotoMono;
          font-size: 16px;
          font-weight: bold;
          font-stretch: normal;
          font-style: normal;
          letter-spacing: 1.4px;
          text-align: center;
          vertical-align: middle;
          color: rgba(0, 0, 0, 0.87);
        }
        
        .footer .right {
           position: absolute;
           top: 0;
           right: 12px;  
           width: 161px;   
        }
        
        .headline-container {
            height: 104px;
            background-image: url(${backgroundImage});
            background-size: cover;
            background-repeat: no-repeat;
            position: relative;
        }

        .headline-box {
            margin: 19px 32px 0 32px;
            width: 491px;
            height: 72px;
        }

        .headline {
            padding: 12px;
            background-color: hsla(143, 42%, 49%, 0.90);
            color: white;
            line-height: 24px;
            font-size: 24px;
            text-align: center;
            vertical-align: middle
        }

        .coupon-description {
            font-family: OpenSans-Semibold;
            position: relative;
            padding: 0 32px;
            width: 491px;
            line-height: 14px;
            font-size: 12px;
            color: #fefefe;
            height: 68px;     
            text-align: center;       
        }
        
        .store-information {
            margin-top: 12px;
            position: relative;
            font-family: OpenSans-Semibold;
            padding: 0 32px;
            width: 491px;
            line-height: 14px;
            font-size: 12px;
            color: #fefefe;
            height: 56px;
            
            text-align: center;
        }
        
        .store-information .address {
            font-family: OpenSans;
            color: rgba(254, 254, 254, 0.87);
        }

        .scaled-logo {
             font-family: OpenSans;
             font-size: 12px;
             font-weight: normal;
             font-stretch: normal;
             font-style: normal;
             line-height: normal;
             letter-spacing: normal;
             text-align: center;
             color: #1d1d1b;
             width: 100%;
             height: 100%;
        }
    </style>
</head>
<body>

<section class="dina4">

<div class="upper-part">
    <div class="header">
        <div class="left">
            <img class="scaled-logo" src="${platformLogo}"/>
        </div>
        
        <div class="center header-text">
            <p>
                <b style="font-weight: bold; font-size: 24px; vertical-align: middle; line-height: 24px;">GUTSCHEIN</b><br/><br/>
            </p>
            <p style="font-size: 12px; line-height: 16px; vertical-align: middle;">
                ${couponText}
            </p>
        </div>
        
        <div class="right">
            <img class="scaled-logo" src="${storeLogo}"/>
        </div>
    </div> 
    
    <div class="headline-container">
        <div class="headline-box">
            <div class="headline">
                ${couponName}
            </div>
        </div>
    </div>
</div>

<div class="lower-part">
    <div class="coupon-description">
        ${couponDescription}
    </div>
    
    <div class="store-information">
        Unser ${storeName} Team freut sich auf deinen Besuch!
        <br />
        <p class="address">${storeStreet} ${storeHouseNumber} ${storeZipCode} ${storeCity}</p>
    </div>
    
    <div class="footer">
        <div class="left">
            Der Gutscheinwert beträgt ${couponValue}€ 
            <br />
            <br />
            Dieser Gutschein ist gültig bis ${validUntil} 
        </div>
        
        <div class="center">
            <p class="code">${couponNumber}</p>
        </div>
        
        <div class="right">
            <img class="scaled-logo" src="${wlLogo}"/>
        </div>
    </div>
</div>
</section>
</body>
</html>