//
//  GuestTableviewCell.h
//  P2PDinner
//
//  Created by selvam on 2/6/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GuestTableviewCell : UITableViewCell
{
    int guestCount;
    IBOutlet UILabel *guestCountDisplayLable;
    IBOutlet UISegmentedControl *guestActionButton;

}
@property(nonatomic)int guestCount;
- (IBAction)guestSegmentAction:(id)sender;
@end
