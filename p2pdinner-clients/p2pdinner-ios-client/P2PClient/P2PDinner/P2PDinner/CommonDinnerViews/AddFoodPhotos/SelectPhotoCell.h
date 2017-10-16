//
//  SelectPhotoCell.h
//  P2PDinner
//
//  Created by Selvam M on 3/30/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol SelectPhotoDelegate<NSObject>
-(IBAction)selectPhotoAction:(id)sender;
-(void)removePhotoOf:(UITableViewCell *)cell atTag:(NSUInteger)tag;
-(void)tableviewReLoad;
@end
@interface SelectPhotoCell : UITableViewCell
{
    IBOutlet UIButton *addPhoto1;
    IBOutlet UIButton *removePhoto1;
    IBOutlet UIButton *addPhoto2;
    IBOutlet UIButton *removePhoto2;

}
@property(nonatomic,strong)id<SelectPhotoDelegate> delegate;
- (IBAction)removePhotoFromCell:(id)sender;
- (IBAction)addPhotoAction:(id)sender;

@end
