//
//  TitleTableViewCell.h
//  P2PDinner
//
//  Created by Selvam M on 3/20/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>

@interface DescriptionTableViewCell : UITableViewCell
{
    IBOutlet UITextView *DescrTextView;
}
-(void)initialSetUpCell;
@end
