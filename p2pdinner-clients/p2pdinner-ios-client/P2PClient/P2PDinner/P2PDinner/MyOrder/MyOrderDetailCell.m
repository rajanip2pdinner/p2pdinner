//
//  MyOrderDetailCell.m
//  P2PDinner
//
//  Created by Selvam M on 11/24/16.
//  Copyright © 2016 P2PDinner. All rights reserved.
//

#import "MyOrderDetailCell.h"
#import "Utility.h"
#import <EventKit/EventKit.h>
#import "AppDelegate.h"
#import "StringConstants.h"

@implementation MyOrderDetailCell
- (void)setCartDetailValues:(CarRecivedItemDetail *)cartDetail{
    _cartDetail=cartDetail;
}
- (IBAction)locateInMap:(id)sender{
    NSArray *addressArray=[NSArray arrayWithObjects:_cartDetail.address_line1,_cartDetail.address_line2,_cartDetail.city,_cartDetail.state,nil];
    NSString *location=[addressArray componentsJoinedByString: @","];
    location =[NSString stringWithFormat:@"http://maps.apple.com/?q=%@",location];
    NSURL *url = [[NSURL alloc] initWithString:[location stringByAddingPercentEncodingWithAllowedCharacters:NSCharacterSet.URLQueryAllowedCharacterSet]];
    [[UIApplication sharedApplication] openURL:url];
}
- (IBAction)addToCalenderEvent:(id)sender{
    EKEventStore *store = [EKEventStore new];
        [store requestAccessToEntityType:EKEntityTypeEvent completion:^(BOOL granted, NSError *error) {
            if (!granted) {
                [[[UIAlertView alloc]initWithTitle:kAlert_Title_Access_Error message:kAlert_Message_Access_Error delegate:nil cancelButtonTitle:kAlert_Ok otherButtonTitles:nil]show];
                return;
            }
            EKEvent *event = [EKEvent eventWithEventStore:store];
            event.title = kMy_P2PDinner_Text;
            event.startDate = [Utility epochToDate:_cartDetail.startTime];
            event.endDate = [Utility epochToDate:_cartDetail.endTime];
            NSTimeInterval interval = 60* -15;
            EKAlarm *alarm = [EKAlarm alarmWithRelativeOffset:interval];
            [event addAlarm:alarm];
            NSArray *addressArray=[NSArray arrayWithObjects:_cartDetail.address_line1,_cartDetail.address_line2,_cartDetail.city,_cartDetail.state,nil];
            event.location=[addressArray componentsJoinedByString: @","];
            
            AppDelegate *appdelegate=(AppDelegate *)[[UIApplication sharedApplication] delegate];
            NSString *locationString=appdelegate.localLocation;
            if (!locationString||!(locationString.length>0)) {
                locationString=[[NSLocale preferredLanguages] objectAtIndex:0];
            }
            NSLocale* localPrice = [[NSLocale alloc] initWithLocaleIdentifier:locationString];
            NSNumberFormatter *fmtr = [[NSNumberFormatter alloc] init];
            [fmtr setNumberStyle:NSNumberFormatterCurrencyStyle];
            [fmtr setLocale:localPrice];
            
            
            
            event.notes=[NSString stringWithFormat:@"%@,%@ \n Conf # %@ for %d plates, %@ \n%@ \n\n\nThank you for buying your dinner today through P2PDinner\n\n",_cartDetail.title,[fmtr stringFromNumber: _cartDetail.totalPrice],_cartDetail.passCode,[_cartDetail.orderQuantity intValue],_cartDetail.deliveryType,[addressArray componentsJoinedByString: @","]];
            event.calendar = [store defaultCalendarForNewEvents];
            NSError *err = nil;
            [store saveEvent:event span:EKSpanThisEvent commit:YES error:&err];
            if (!err) {
                [[[UIAlertView alloc]initWithTitle:kAlert_Title_EventAdded message:kAlert_Message_EventAdded delegate:nil cancelButtonTitle:kAlert_Ok otherButtonTitles:nil]show];
                return;
            }
        }];
   }
@end
