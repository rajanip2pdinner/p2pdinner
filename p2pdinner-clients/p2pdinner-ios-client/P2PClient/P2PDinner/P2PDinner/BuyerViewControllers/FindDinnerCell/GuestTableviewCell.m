//
//  GuestTableviewCell.m
//  P2PDinner
//
//  Created by selvam on 2/6/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "GuestTableviewCell.h"
#import "AppConstants.h"
@implementation GuestTableviewCell
@synthesize guestCount;
- (void)awakeFromNib {
    // Initialization code
    guestCount=GuestMinValue;
}
- (IBAction)guestSegmentAction:(id)sender{
    UISegmentedControl *seg=(UISegmentedControl *)sender;
    if (seg.selectedSegmentIndex==0) {
        [self performSelector:@selector(guestDecrementAction:) withObject:sender afterDelay:0.5];
    }
    else{
        [self performSelector:@selector(guestIncrementAction:) withObject:sender afterDelay:0.5];
    }
    
}
- (void)guestDecrementAction:(id)sender{
    [guestActionButton setSelectedSegmentIndex:-1];
    guestCount--;
    [self setvalueForGuestCount:guestCount];
}
- (void)guestIncrementAction:(id)sender{
    [guestActionButton setSelectedSegmentIndex:-1];
    guestCount++;
    [self setvalueForGuestCount:guestCount];
}

- (void)setvalueForGuestCount:(int)count{
    if (count<GuestMinValue) {
        guestCount=1;
        return;
    }else if (count>GuestMaxValue)
    {
        guestCount=GuestMaxValue;
        return;
    }else {
        if (count==1) {
            guestCountDisplayLable.text=[NSString stringWithFormat:OneGuest];
            return;
        }
        guestCountDisplayLable.text=[NSString stringWithFormat:MoreThanGuest,count];
    }
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
    
    // Configure the view for the selected state
}

@end
