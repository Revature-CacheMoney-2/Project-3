
import { useEffect, useState } from "react";

// @author Max Hilken, Mika Nelson, Cullen Kuch

export default function Notification({unreadNotifications}){
    return(
        <div>
            {console.log(unreadNotifications)}
            <div className = "NotifParent">
            {unreadNotifications.length > 0 ? (
                    unreadNotifications.map((noti) => (
                    <div className = "NotificationDiv" key = {noti.notif_id}>
                        <p>
                            {noti.subject}
                        </p>
                        <p>
                            {noti.notif_text}
                        </p>
                        <p>
                            {noti.date}
                        </p>
                    </div>
                    ))
                ) : (
                    <h4 style={{ color: "black" }}>No notifications</h4>
                )}
                
            </div>
            {/* <OverlayTrigger
            trigger="click"
            key="left"
            placement="left"
            overlay={
            <Popover id={`popover-positioned-left`}>
                <Popover.Header as="h3">{`Notifications`}</Popover.Header>
                <Popover.Body className="popover-notifications">
                {unreadNotifications.length > 0 ? (
                    unreadNotifications.map((noti) => (
                    <p>
                        {noti}
                    </p>
                    ))
                ) : (
                    <h4 style={{ color: "white" }}>No notifications</h4>
                )}
                </Popover.Body>
            </Popover>}  />
            <Button variant="secondary">Popover on </Button>
            <OverlayTrigger/> */}
        </div>)
}