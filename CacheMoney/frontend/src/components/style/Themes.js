// import dark from "../../image/Dark_Background_image-01.png"
// import light from "../../image/CacheMoney_MoneyBackground_Smaller.png"
// import logo from "../../image/CacheMoney-logo.png"
// import logoDark from "../../image/CacheMoney-logo-dark.png"
import config from "../../config"

export const lightTheme = {
    gentle: "#6c896a",
    background: "#fff",
    text: "#fff",
    shadow: "#3e4f3d",
    bread: "var(--green-dark-wallstreet)",
    highlight: "#d3a940",
    backlight: "#d3a940",
    ultrahighlight: "#f6ba65",
    greyness: "#4c4c4b",
    specialButtons: "#d3a940",
    error: "#cc3040",
    splashBackground: `url(${config.url_img}frontend-images/Background_image-01.png)`,
    moneyBackground: `url(${config.url_img}frontend-images/CacheMoney_MoneyBackground_Smaller.png)`,
    cacheMoney: `url(${config.url_img}frontend-images/CacheMoney-logo.png)`,
}

export const darkTheme = {
    gentle: "#4c4c4b",
    background: "#4c4c4b",
    text: "#6c896a",
    shadow: "#101010",
    bread: "#3e4f3d",
    highlight: "#3d703a",
    backlight: "#101010",
    ultrahighlight: "#6c896a",
    specialButtons: "#3e4f3d",
    greyness: "#4c4c4b",
    error: "#cc3040",
    splashBackground: `url(${config.url_img}frontend-images/Dark_Background_image-01.png)`,
    moneyBackground: `url(${config.url_img}frontend-images/CacheMoney_MoneyBackground_Dark.png)`,
    cacheMoney: `url(${config.url_img}frontend-images/CacheMoney-logo-dark.png)`,
}