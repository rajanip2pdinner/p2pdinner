//
//  AddTimeController.h
//  P2PDinner
//
//  Created by Selvam M on 4/2/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AvialbleTimeTableCell.h"
#import "AcceptOrdersTableCell.h"
#import "DatePicker.h"
#import "ItemDetails.h"

@interface AddTimeController : UIViewController<DatePickerDelegate,AcceptOrdersDelegate,AvialbleTimeDelegate>
{
    IBOutlet UITableView *addTimeTableview;
    NSDate *startDate;
    NSDate *endDate;
    NSDate *closeDate;
    
    //ToGetValues
    UILabel *timeLable;

    AvialbleTimeTableCell *availCell;
    AcceptOrdersTableCell *accepCell;
    
    NSDate *picSelectedDate;


}
@property(nonatomic,strong) ItemDetails *itemDetails;
@property(nonatomic,assign) BOOL isBuyerFlow;
@end
