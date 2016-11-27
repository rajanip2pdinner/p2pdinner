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

@implementation MyOrderDetailCell
- (void)setCartDetailValues:(CarRecivedItemDetail *)cartDetail{
    _cartDetail=cartDetail;
}
- (IBAction)addToCalenderEvent:(id)sender{
    EKEventStore *store = [EKEventStore new];
        [store requestAccessToEntityType:EKEntityTypeEvent completion:^(BOOL granted, NSError *error) {
            if (!granted) {
                [[[UIAlertView alloc]initWithTitle:@"P2P Access Error" message:@"Please enable settings privacy allow to access calender" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil]show];
                return;
            }
            EKEvent *event = [EKEvent eventWithEventStore:store];
            event.title = @"My P2PDinner";
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
                [[[UIAlertView alloc]initWithTitle:@"P2P Event Added" message:@"Added into calendar" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil]show];
                return;
            }
        }];
   }
@end
